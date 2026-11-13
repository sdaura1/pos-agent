# S5 — Kozen Adapter and Device Bring-Up

Status: blocked
Slice: S5
Depends on: S1–S4, hardware, firmware, certification, keys
Effort: 5–8 developer-days (once unblocked)
Related: [`04-ports-and-fakes.md`](04-ports-and-fakes.md), [`03-emv-session.md`](03-emv-session.md)

## 1. Goal

Implement the ports against the Kozen Financial SDK. Nothing outside
`data/device/kozen`, `di` and the app manifest should need to change.

## 2. Preconditions checklist

- [ ] Kozen terminal model and firmware identified.
- [ ] Installed FinancialService version recorded.
- [ ] SDK version chosen to match that service (see section 3).
- [ ] `SUPER_PERMISSIONS_*` granted to the APK; signing/whitelisting confirmed.
- [ ] EMV parameters received: AID list, CAPK list, terminal capabilities, TAC,
      limits, DRL, currency 566.
- [ ] Key injection method agreed with the acquirer; test keys loaded.
- [ ] Certification plan and test cards available.
- [ ] CTMS test host, credentials, key download (TSK) and the hash
      specification available.
- [ ] Location permission and a working fix, or an agreed DE120 fallback.
- [ ] Confirmed whether TerminalManager and Component SDKs are needed at all.

## 3. Artifacts and versions

Ship exactly one version of each SDK. The pairs currently in `app/libs` are
alternatives; a second version produces duplicate `com.kozen.*` classes.

| SDK | Candidate | Service it expects |
| --- | --- | --- |
| FinancialLib | 1.4.0 | FinancialService 1.2.x |
| TerminalManagerLib | 1.2.0 | TerminalManagerService 1.1.x |
| ComponentLib | 1.3.0 | ComponentService 1.2.x |

Confirm against the device; the client performs a version handshake at init and
refuses to start on a mismatch. Newer vendor builds exist (Financial 1.7.0,
TerminalManager 1.6.0, Component 1.6.0) — prefer the newest set the firmware
supports rather than these mid-2025 builds.

Packaging notes:

- Each AAR contains `classes.jar` (client) and `libs/<api>.jar` (public API).
  Compile against the latter; if the build does not expose AAR-nested jars,
  extract them as file dependencies.
- Do not add the AAR `aidl/` folder to source sets; parcelables ship compiled.
- `minSdk 24` satisfies the SDK's 23.
- Financial and Component AAR manifests declare `<queries>` for their services;
  TerminalManager does not. Add package visibility for the terminal service when
  targeting Android 11+.
- Keep all vendor dependencies in a `kozen` product flavor so emulator and CI
  builds never load them.

## 4. Components

| Port | Adapter | Notes |
| --- | --- | --- |
| `CardAcceptor` | `KozenCardAcceptor` | `startTransaction` + `IEmvListener`, reply via `set*Response` |
| `PinPad` | `KozenPinPad` | `startInputPin` with app-supplied views, `PinpadInputCallback` |
| `ReceiptPrinter` | `KozenReceiptPrinter` | `open`, `addText`, `startPrint`, `close`; map blocks to print lines |
| `TerminalIdentity` | `KozenTerminalIdentity` | serial, TUSN, CSN, EMV kernel and service versions |
| `TerminalProvisioning` | `KozenSdkInitializer` | init, key exchange, version handshake, reconnect, unavailable reasons |

Also in `data/device/kozen/mapper`:

- `BundleMapper`: the only place that knows vendor bundle keys
  (`transAmount`, `transType`, `cardTrack2`, `cardPan`, EMV constraints, …).
- `ErrorMapper`: numeric vendor codes to `DomainError` codes; table-driven.
- `EmvConfig`: AIDs, CAPKs, terminal parameters loaded from signed configuration
  rather than hardcoded.
- `PrintMapper`: receipt blocks to `TextPrintLine`, `BarcodePrintLine`,
  `BitmapPrintLine`.

## 5. Initialisation

- Initialise once from `Application` with the application context. The engine
  holds a static context and a static binder; do not initialise per Activity.
- `InitListener.onResult(code, message)` with `0` meaning success; map every
  failure to `Readiness.Unavailable` with a stable reason.
- Re-initialise when the service disconnects; the callback is also used for
  disconnection in some SDK versions.
- Financial 1.4.0 exposes no public `deInit`; treat the engine as a process
  singleton. Component 1.3.0 has `deInit`.
- Never block app startup: expose `Unprovisioned`, then `Provisioned` or `Failed`.
  The purchase button is disabled until ready.
- Convert binder callbacks to coroutines inside the adapter; never call
  `suspend` functions from a binder thread directly.

## 6. EMV mapping rules

- `onSelectApplication` → `SelectApplication` prompt → reply with
  `setSelectApplicationResponse(position)`.
- `onConfirmCardInfo` → `ConfirmCardInfo` → `setCardInfoResponse`.
- `onRequestInputPin` → `RequestPin` → PIN pad → `setPinResponse`.
- `onRequestOnlineProcess` → `RequestOnline` → use case authorises → 
  `setOnlineResponse`.
- `onTransactionResult` → capture or failure.
- Enable transaction encryption on the kernel before the first transaction:
  `openEncrypt`, `useEncryptKsn`, `encryptTrackUseBCD` and the relevant
  `encrypt*` switches, so plaintext tracks never reach the app.
- Adapter guard: if a returned track or PAN looks like a full PAN and
  encryption was expected, fail closed, log no data, and surface a terminal
  error.

### NIBSS-facing obligations

- **Issuer scripts and authentication data.** DE55 response tags `91`, `71`
  and `72` must be passed back through `setOnlineResponse`; otherwise issuer
  authentication and script processing are skipped.
- **Capture metadata.** Expose entry mode, CVM, PIN length and terminal traits
  so the adapter can assemble DE22 and DE123. Do not derive these from the
  host response.
- **EMV parameters.** AIDs, CA keys, TAC and limits can arrive through CTMS
  0800 downloads (DE63 tags 13–40). Feed them into `setAidList`, `setCapkList`
  and `setTerminal`; fall back to signed local configuration only if NIBSS
  agrees.
- **Location.** DE120 needs coordinates at transaction time. Use the
  TerminalManager location module; the card adapter should receive them, not
  acquire them.
- **Keys and hashing.** The TSK and the DE128 hash are app-side concerns. The
  Kozen security module handles PIN and track keys, not the CTMS message hash.


## 7. Security and compliance

- Keys are injected through the acquirer's process (`SecurityManager`), not by
  app code.
- No PAN, track, PIN block or KSN in logs, crash reports, analytics or files.
- Debug logging of `Bundle`s is disabled by default and redacted.
- `FLAG_SECURE` while PIN entry is shown.
- R8: the AARs ship empty consumer rules. Verify binding and callbacks survive
  minification; add keep rules for adapters if needed.

## 8. Tasks

- [ ] Add the chosen AARs; remove or archive the unused versions.
- [ ] Add the `kozen` product flavor and a `mock` flavor for CI/emulator.
- [ ] Implement `KozenSdkInitializer` and wire readiness into DI.
- [ ] Implement `KozenCardAcceptor` and the EMV session bridge.
- [ ] Implement `KozenPinPad` with the required view IDs and physical keypad.
- [ ] Implement `KozenReceiptPrinter` and `PrintMapper`.
- [ ] Implement `KozenTerminalIdentity`.
- [ ] Implement `BundleMapper` and `ErrorMapper` with fixtures captured on
      device.
- [ ] Map DE55 request tags into the kernel and DE55 response tags back into
      `setOnlineResponse`.
- [ ] Expose capture metadata (entry mode, CVM, PIN length) for DE22/DE123.
- [ ] Provide location coordinates for DE120 from TerminalManager.
- [ ] Apply downloaded CTMS EMV parameters (AIDs, CA keys, TAC, limits) to the
      kernel.
- [ ] Load EMV configuration and reject an unsigned or incomplete config.
- [ ] Add manifest package visibility for the terminal service.
- [ ] Replay the shared contract suite against the device.
- [ ] Run the device acceptance checklist below.

## 9. Device acceptance checklist

- [ ] Init succeeds; version handshake passes; provisioning is `Provisioned`.
- [ ] Contact purchase, contactless purchase, magstripe fallback.
- [ ] Application selection with a multi-AID card.
- [ ] Online PIN and signature CVM paths.
- [ ] Host decline and host timeout (`UNKNOWN` + reconcile).
- [ ] Cancel at card wait, PIN and online.
- [ ] Card removed mid-session; card left powered off afterwards.
- [ ] Printer: approved, declined, reprint, out-of-paper.
- [ ] No plaintext card data in logcat during any of the above.
- [ ] App survives service disconnect and reinitialises.

## 10. Acceptance criteria

- The contract suite passes on hardware without changes above
  `data/device/kozen`.
- The `mock` flavor still builds and runs the fake flow.
- No vendor type is imported outside the adapter package.
- Plaintext card data never leaves the adapter.
