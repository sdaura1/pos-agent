# Risks, Decisions and Assumptions

Status: living document
Related: [`01-overview.md`](01-overview.md)

## 1. Decisions needed

| # | Decision | Options | Recommendation | Status |
| --- | --- | --- | --- | --- |
| D1 | Persistence | Room / SQLDelight | Room | Open |
| D2 | NIBSS transport | REST / ISO 8583 over TCP | ISO 8583 over TCP — settled by specification v1.0.2 | Settled |
| D3 | PIN UI ownership | App-rendered views (SDK requirement) / SDK default | App-rendered, isolated behind `PinPad` | Open |
| D4 | Offline policy | Queue and reconcile / block purchases | Block new purchases, reconcile unknowns | Open |
| D5 | Receipt content and copies | Merchant only / merchant + customer | Merchant + customer, both configurable | Open |
| D6 | Slice delivery | One change set / slice by slice behind a flag | Slice by slice | Open |
| D7 | Refund/reversal EMV fields | Include / omit | Follow NIBSS spec; omit if not required | Open |
| D8 | Ship TerminalManager SDK | Yes / no | Only if device provisioning or OTA is needed | Open |
| D9 | Hash composition and TSK seeding | Await NIBSS | Implement behind `MessageHasher`; blocking for S4 | Open |
| D10 | Key management model | Master/session / DUKPT | Follow the acquirer's mandate; TSK required either way | Open |
| D11 | DE120 when no location fix | Cached fix / block sale | Block sale; a cached fix needs a maximum age | Open |
| D12 | Terminal provisioning source | 0800 download / signed local config | 0800 download for AIDs, CA keys, TAC, limits | Open |
| D13 | 0210 layout without DE38/DE39 | Follow spec / assume present | Assume present; confirm with NIBSS | Open |

Defaults are provisional until accepted; each affects the docs that reference
it.

## 2. Risk register

| # | Risk | Likelihood | Impact | Mitigation |
| --- | --- | --- | --- | --- |
| R1 | Vendor bundle keys differ by SDK version | Medium | High | All keys in one mapper; pin SDK; capture device fixtures |
| R2 | SDK returns plaintext tracks when encryption is misconfigured | Medium | Critical | Enable kernel encryption; adapter fails closed; redaction tests |
| R3 | Cancel/reply race leaves the kernel or card powered on | Medium | High | Reply-once enforcement; cancel answers pending prompt; always stop |
| R4 | Service version mismatch blocks init on the fleet | High | High | Version handshake; readiness state; choose SDK per firmware |
| R5 | `targetSdk 37` package visibility for TerminalManager | Medium | Medium | Add `<queries>`; verify binding on device |
| R6 | AGP 9 / Hilt / KSP / Room incompatibility | Low | Medium | Verify minimal Room wiring before schema work |
| R7 | Timeout followed by late approval causes double charge | Medium | Critical | `UNKNOWN` + reconcile by reference; never auto-retry |
| R8 | EMV certification changes parameters, forcing code changes | Medium | Medium | Keep AID/CAPK/TAC/limits in data, not code |
| R9 | PIN surface leaks or is captured by screenshots | Low | Critical | `FLAG_SECURE`; no PIN reads; isolated surface; review |
| R10 | Scope creep into settlement/reporting | Medium | Medium | Non-goals are explicit; defer to a later plan |
| R11 | DE120 mandatory but no location fix available | Medium | High | Block the sale, or agree a cached-fix policy with NIBSS |
| R12 | Hash composition unspecified beyond SHA-256 + TSK | High | Critical | Keep it behind `MessageHasher`; obtain a known-answer vector from NIBSS |
| R13 | No transactions until TSK/key download succeeds | High | High | Provision at start-up; show terminal unavailable; retry sign-on |
| R14 | 0210 layout omits DE38/DE39 | Medium | High | Parse leniently; confirm the layout with NIBSS before certification |
| R15 | Host deadline is short (spec example 2 s) | High | High | Use DE62 tag 04, persistent socket, reversal on timeout |
| R16 | Issuer scripts/auth data not applied to the kernel | Medium | High | Map DE55 tags 91/71/72 into `setOnlineResponse`; device test |
| R17 | Bitmap/DE55 wire-length ambiguity | Medium | High | Build vectors against the test host early; do not guess |

## 3. Assumptions

- Single `:app` module, Hilt, Compose, `Response`/`DomainError` conventions, and
  the layer rules in `ARCHITECTURE.md`.
- One developer familiar with the codebase; estimates are developer-days.
- Room is available for use; there is no ban on new dependencies.
- The acquirer supplies EMV parameters and keys; the app never generates them.
- The NIBSS host supports query-by-reference for reconciliation.
- The NIBSS CTMS interface is ISO 8583 over TCP as specified in v1.0.2; there is
  no REST variant for POS.
- The terminal can obtain a location fix at transaction time.
- Kozen hardware is the only terminal target for v1.

## 4. Open questions

For NIBSS:

- Exact bitmap wire length (16 vs 32 hex characters) and whether a TPDU is used.
- Presence of DE38 and DE39 in the 0210 layout.
- Hash composition: field ordering, padding, encoding and how TSK seeds SHA-256.
- DE55 length semantics (hex characters vs bytes) and tag ordering.
- Reversal rules: `0420` vs `0421`, repeat count, and timing after a timeout.
- Whether POS uses advice messages (`0120`/`0220`) at all.
- Accepted DE120 fallback when no GPS fix is available.
- DUKPT versus master/session for this acquirer, and the provisioning procedure.
- Test host, credentials, certification scripts and expected test results.

For the acquirer/host:

- Which EMV data is mandatory on authorisation, reversal and refund.
- Expected behaviour for duplicate submissions and late approvals.

For the vendor:

- Which SDK set matches the fleet firmware, and the exact service versions.
- How `SUPER_PERMISSIONS_*` are granted and how the APK is whitelisted.
- Key injection procedure and whether test keys are available.
- Whether the secondary screen or physical keyboard is in scope for this app.

## 5. Change log

| Date | Change |
| --- | --- |
| 2026-11-13 | Initial plan and slice breakdown |
| 2026-11-13 | Added NIBSS CTMS specification digest; transport settled as ISO 8583 over TCP; added hash, key download, DE120 and reversal requirements |
| 2026-11-13 | S4a-1 implemented: provisioning port, validated state machine, readiness gate, fake and production placeholder |
| 2026-11-13 | S4a-2 implemented: CTMS framing, jpos packager, codec, DE62/DE63 TLV, socket channel with STAN correlation and failure classification |
| 2026-11-13 | S4a-3 implemented: key aliases, in-memory key store, KCV, SHA-256/hex utilities, fail-closed hash interface |
| 2026-11-13 | Renamed key-exchange types for clarity: `NibssConnection`, `Iso8583Codec`, `TaggedFieldCodec`, `KeyCheckValue`, `ProvisioningResult`, `ProvisioningFailureReason`; split mapper and SHA-256 utilities |
| 2026-11-13 | S1.1 implemented: `CardCapture`, `EmvData`, `PinBlockRef`, `GeoCoordinates`, `NfcData`, `EmvHostResponse`, `MessageReasonCode`, `ReplacementAmounts`, terminal traits, and DE90 correlation fields |
