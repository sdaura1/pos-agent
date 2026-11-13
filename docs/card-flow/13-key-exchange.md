# S4a — Key Exchange and Terminal Provisioning

Status: in progress (S4a-1, S4a-2 and S4a-3 implemented)
Implementation notes: [`14-key-exchange-implementation.md`](14-key-exchange-implementation.md)
Slice: S4a
Depends on: NIBSS key-exchange detail, key ownership decision, test host
Effort: 6–10 developer-days once unblocked; S4a-1 is unblocked
Related: [`08-nibss-adapter.md`](08-nibss-adapter.md), [`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md), [`11-risks-and-decisions.md`](11-risks-and-decisions.md)

## 1. Goal

Make the terminal cryptographically ready to transact with NIBSS CTMS: load or
derive the required keys, download terminal/EMV parameters, prove readiness,
and gate every purchase on it.

This is a prerequisite for live traffic, not a prerequisite for building the
transaction flow. The provisioning contract is defined now so the transaction
work can proceed against a fake.

## 2. Why the specification is not enough

The CTMS spec names the hierarchy and the processing codes, but not the wire
payloads:

| Known | Unknown |
| --- | --- |
| CTMK → TMK → TPK/TSK, or BDK/DUKPT | Key-block format and encryption mode per key |
| 0800 codes `9A`, `9B`, `9G`, `9I`, `9J` | Exact DE layout for each download |
| DE62 tag `57` key serial number | Field contents, TLV tags and ordering |
| DE63 tags for AID/CA-key download | Which tags are mandatory, and the response shape |
| TSK seeds the DE64/DE128 SHA-256 | Hash composition, padding, encoding |
| DE64 when no secondary fields, else DE128 | Whether provisioning messages are hashed at all |
| Reversal/advice semantics | Key rotation, retry and repeat rules |

One captured provisioning trace plus one hash vector removes most of this.

## 3. Key hierarchy and ownership

| Key | Purpose | Typical owner | Possible channel |
| --- | --- | --- | --- |
| CTMK | Clear terminal master key | Acquirer/PTSP | Key ceremony or loader, never the wire |
| TMK | Protects downstream keys | Terminal | 0800 `9A`, encrypted under CTMK |
| TPK | PIN block encryption | Kozen security module / PTSP | 0800 `9G` or `SecurityManager.writeKey*` |
| IPEK (track 2 / EMV) | DUKPT derivation | Kozen security module / PTSP | 0800 `9I`/`9J` or vendor injection |
| BDK | DUKPT base derivation | Acquirer | Injected in components, never transmitted |
| **TSK** | **Seeds the message hash** | **App** | 0800 `9B` |
| AIDs/CA keys/TAC | EMV configuration | CTMS | 0800 `9E`/`9F`, DE63 tags 13–40 |

Decision D10 (key ownership) determines whether this slice implements the full
hierarchy or only TSK plus parameter download. Both need the same provisioning
state machine.

## 4. Wire map

| Step | Message | Code | Purpose |
| --- | --- | --- | --- |
| 1 | 0800 → 0810 | `9A` | Terminal master key |
| 2 | 0800 → 0810 | `9G`, `9I`, `9J` | PIN key and IPEKs (if app-owned) |
| 3 | 0800 → 0810 | `9B` | Terminal session key for hashing |
| 4 | 0800 → 0810 | `9C` | Terminal parameters |
| 5 | 0800 → 0810 | `9E`, `9F` | CA keys and AIDs |
| 6 | 0800 → 0810 | `9D` | Call home / sign-on |
| 7 | 0800 → 0810 | `9H` | Daily transaction report (optional) |

Order and mandatory steps must be confirmed; the table is the working model.
Every download must end in DE39 `00` before the next step runs.

## 5. Sub-slices

| Sub-slice | Content | Status |
| --- | --- | --- |
| S4a-1 | Provisioning port, state machine, readiness gate, fake, DI placeholder | Done |
| S4a-2 | 0800 framing, packager, codec, TLV, channel and correlation | Done; payload builders blocked |
| S4a-3 | Key store, KCV helpers, TSK storage, hash interface | Done |
| S4a-4 | Real key download against the test host | Blocked (trace, host, keys) |
| S4a-5 | Parameter download into the EMV kernel | Blocked (trace, vendor mapping) |

### S4a-1 tasks

- [x] Add `ProvisioningState`, `ProvisioningFailureReason`, `ProvisioningResult`.
- [x] Add the validated `ProvisioningStateMachine`.
- [x] Add the `TerminalProvisioning` port.
- [x] Add `EnsureTerminalProvisionedUseCase` with stable error codes.
- [x] Add `FakeTerminalProvisioning` for tests and emulator runs.
- [x] Add a production placeholder that reports unavailable rather than
      pretending to be ready.
- [x] Bind the placeholder in Hilt; keep the fake test-only.
- [x] Unit tests for transitions, idempotency, concurrency and error mapping.

### S4a-2 tasks (implemented)

- [x] jpos packager for network management: fields 3, 7, 11, 12, 13, 41, 53,
      62, 63, 64.
- [x] Framing: 2-byte binary length prefix, ASCII bitmap.
- [x] Connection, correlation by STAN, reconnect and backoff.
- [x] DE62/DE63 TLV encoder/decoder with unit tests.
- [x] Envelope pack/parse for 0800/0810 with a golden fixture.
- [ ] Semantic payload builders per processing code (blocked on the key payload
      layout from NIBSS).
- [x] DE64/128 hash hook behind `MessageHasher` (S4a-3).

### What S4a-2 produced

| Component | Behaviour |
| --- | --- |
| `MessageFraming` | 2-byte big-endian length, stream-safe reads, 65,535-byte cap |
| `Iso8583Packager` | Loads the single `jpos.xml` packager from resources |
| `Iso8583Codec` | Pack/unpack, framed read/write, message factory |
| `TaggedFieldCodec` | `tag(2)+length(3)+value` encode/decode, validation |
| `NibssConnection` | Single-flight socket exchange, STAN correlation, reconnect |
| `NibssExchangeException` | `NotSent` (safe retry), `Unknown` (reverse/reconcile), `Protocol` |

A golden test pins the 0800 wire format, including the bitmap
`2238000000800005` for DE3/7/11/12/13/41/62/64. A loopback server test proves
framing, packing, correlation and the failure classification end to end.

Note for release builds: the jpos packager instantiates field packagers by
class name from XML, so R8 needs keep rules for `org.jpos.iso.*` before
minification is enabled.

The existing `jpos.xml` was consolidated as the single dialect packager. Two
spec mismatches were corrected while doing so: DE52 and DE53 are now ASCII hex
(16/96 characters) rather than raw binary, and DE120 geo coordinates were added
(it is mandatory on every request in CTMS v1.0.2). A secondary-bitmap test
covers DE120 and DE128.

### S4a-3 tasks (implemented)

- [x] `SecretKeyAlias` and `SecretKeyStore` with copy-on-read/write and
      zero-on-remove semantics.
- [x] `InMemorySecretKeyStore` for the current memory-only decision.
- [x] `KeyCheckValue` for DES and 3DES with known-answer vectors.
- [x] `MessageHasher` interface, fail-closed default and test fake.
- [x] `HexCodec` utilities with NIST SHA-256 vectors and constant-time comparison.
- [x] Hilt bindings for the key store and the unconfigured hasher.
- [ ] Android Keystore-backed store (pending the storage decision).
- [ ] Certified hash composition (awaiting the NIBSS known-answer vector).

### What S4a-3 produced

| Component | Behaviour |
| --- | --- |
| `SecretKeyStore` | Port with copy isolation and zeroing on removal |
| `InMemorySecretKeyStore` | Process-local, mutex-protected, lost on restart |
| `KeyCheckValue` | DES/3DES KCV, 8/16/24-byte keys, constant-time match |
| `MessageHasher` | Hash interface; bound default throws until configured |
| `FakeMessageHasher` | Deterministic test hash; never for live traffic |
| `HexCodec` | Uppercase hex, tolerant decode, SHA-256, constant-time compare |

The hash remains fail-closed: a message is never sent with a guessed DE128.
KCVs verify a download without exposing the key.

## 6. State machine

| State | Meaning | Allowed next |
| --- | --- | --- |
| `Unprovisioned` | No attempt yet | `DownloadingKeys`, `Failed` |
| `DownloadingKeys` | Fetching TMK/TPK/TSK/IPEK | `DownloadingParameters`, `Failed` |
| `DownloadingParameters` | Fetching AIDs, CA keys, TAC, limits | `Provisioned`, `Failed` |
| `Provisioned` | Keys and parameters proven present | `DownloadingKeys` (refresh) |
| `Failed` | Provisioning could not complete | `DownloadingKeys`, `Unprovisioned` |

Rules:

- `Provisioned` is only reachable through a completed parameter download.
- A failure can occur before the first download (service missing) or during any
  step.
- `Provisioned` is not revoked by a service disconnect: keys remain on the terminal.
  Readiness of the *service* is a separate concern.
- `reprovision()` forces a new attempt even when `Provisioned`, for rotation.

## 7. Port and gating

`TerminalProvisioning` exposes the state as a `StateFlow` and two suspend
operations: `ensureProvisioned()` (idempotent) and `reprovision()` (forced).

`EnsureTerminalProvisionedUseCase` maps the outcome to `Response<Unit>` with stable
codes (`TERMINAL_SERVICE_UNAVAILABLE`, `TERMINAL_VERSION_MISMATCH`,
`TERMINAL_KEY_DOWNLOAD_FAILED`, …). The purchase flow calls it before starting
acquisition; the UI disables the start action while not ready and shows the
reason.

Concurrency: `ensureProvisioned()` is single-flight behind a mutex. Two callers must
not start two downloads.

## 8. Storage and security

- Keys never touch logs, analytics, crash reports or backups.
- TSK is held in memory and/or an encrypted store. Memory-only is acceptable
  because a successful sign-on precedes the first sale.
- Key material is compared by KCV, never by printing the key.
- The provisioning service runs as a singleton with the application context.
- A provisioning failure is recoverable: retry, re-onboard, or re-inject.

## 9. Test and certification environment

- Kozen terminal matching the target firmware, with the vendor services
  installed and the app whitelisted.
- NIBSS test host reachable from the device (VPN/allowlist/leased line).
- Test TID, MID, acquirer/forwarding IDs.
- Test CTMK and BDK components; expected KCVs.
- Ability to capture hex traces on both sides, or an NIBSS trace reference.

## 10. Information required before S4a-4

### From NIBSS

1. The key-exchange appendix or terminal-provisioning document.
2. One hex trace of a successful onboarding sequence: `9A`, `9B`, `9G`/`9I`/
   `9J`, `9C`, `9E`/`9F`, with the matching `0810` responses.
3. Exact request/response layouts per code: which DEs, which DE62/DE63 tags,
   whether DE53 or DE64/128 is present, and field ordering.
4. CTMK delivery method and the expected KCV.
5. TMK transport: algorithm, mode, key-block format, variants.
6. Whether the POS or the PTSP/terminal injects TPK and IPEK.
7. TSK: transport, lifetime, rotation, raw or derived, and the exact hash
   composition with a known-answer vector.
8. KSN format and BDK component handling for DUKPT.
9. Success and failure DE39 values for each 0800 code, plus retry/repeat rules.
10. Whether AIDs/CA keys/TAC must be downloaded before the first transaction and
    how the terminal confirms they applied.
11. Whether CTMS pushes key/parameter updates (advice) or only answers requests.
12. Test host address/port, access method, test credentials, onboarding steps
    and a trace/log contact.

### From Kozen / the acquirer

1. Firmware and FinancialService version.
2. Supported schemes: DUKPT-DES, DUKPT-AES, MK/SK, TR-31.
3. Whether the app may write keys, or an RKI/key-loader process is mandatory.
4. Secure storage location and whether a "keys loaded" status can be queried.
5. TUSN/CSN format and whether keys are bound to the device.
6. Which `SecurityManager` calls are enabled for each key type.
7. Key-loader procedure and test-key availability.
8. Model/firmware differences across the fleet.

### Internal decisions

1. Scope: full hierarchy or TSK + parameters only (D10).
2. TSK storage: memory-only or encrypted at rest.
3. Behaviour when provisioning fails: block sales, offline grace, or cached
   parameters.
4. Rotation triggers and who operates them.
5. Ownership of provisioning failures in production support.

## 11. Trace request template

When asking for a trace, request:

- Terminal model, firmware, FinancialService version, TID/MID.
- Direction, timestamp and hex dump including the 2-byte length header and
  bitmap.
- The processing code and DE39 for each exchange.
- KCVs for loaded keys (never the keys themselves).
- Note of whether the link is VPN, leased line or TLS.
- A sample `0200` and its `0210`, with the DE128 value, for the hash vector.

Redact key values but preserve structure, lengths and tags.

## 12. What proceeds without the answers

S4a-1, S4a-2 and S4a-3 are fully implementable and testable in isolation:
state machine, framing, packager, TLV codec, key store, KCV helpers and a
pluggable hash. The transaction flow continues against the fake provisioning
adapter.

## 13. Risks

| Risk | Mitigation |
| --- | --- |
| Key payload layout guessed wrong | Wait for the trace; isolate in one builder and fixtures |
| Hash composition wrong | Known-answer test; one `MessageHasher` implementation |
| PTSP owns more keys than expected | Scope decision before S4a-4; keep port generic |
| Key rotation breaks a live terminal | Refresh path, readiness state, retry, alerts |
| Secrets leak through logs | Redaction tests, no key logging, KCV comparisons only |

## 14. Links

- [`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md) — field formats, 0800 codes, key hierarchy
- [`08-nibss-adapter.md`](08-nibss-adapter.md) — transaction adapter that consumes provisioning
- [`11-risks-and-decisions.md`](11-risks-and-decisions.md) — D9, D10, D11
