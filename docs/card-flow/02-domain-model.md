# S1.1 — Domain Model

Status: in progress (model implemented; acquisition wiring deferred to S1.3)
Slice: S1
Depends on: nothing
Effort: 2–3 developer-days
Related: [`03-emv-session.md`](03-emv-session.md), [`14-key-exchange-implementation.md`](14-key-exchange-implementation.md)

## 1. Goal

Model what the terminal actually produces for one card presentation, without
leaking vendor types or exposing PAN data to the domain.

`PurchaseRequest` previously assumed the reader had already produced a finished
`CardToken` and `MaskedPan`. Real EMV capture also carries entry mode, CVM,
cryptogram data, KSN and a PIN block reference, and the host needs them.

## 2. Design decisions

- `CardToken` stays opaque and holds only the encrypted track-2-equivalent or
  encrypted blob. It never holds a readable PAN.
- EMV TLV is modelled separately as `EmvData` (hex string). TLV can contain
  sensitive tags, so `toString` is redacted even though it is not a raw PAN.
- `MaskedPan` remains the only readable PAN representation and is the only PAN
  value persisted or displayed.
- Capture is separated from authorisation: `CardCapture` describes what was
  read; `CardOperationResult` describes what the host said.
- PIN block and KSN are references only: the domain never validates or derives
  keys.
- Entry mode and CVM are enums, not booleans, so fallback and signature paths
  are explicit.
- The PAN guard uses the **Luhn check** on 13–19-digit runs. A plain length check
  falsely rejects valid AIDs such as `A0000000041010`, which contain long zero
  runs.
- `TerminalTraits` lives in `domain/terminal/model`. It is terminal
  configuration, not part of a card capture, and cross-context references stay
  explicit.
- `DomainError` is unchanged. Capture failures carry stable codes from
  `CardCaptureErrorCode`; adapters map those to `DomainError` types as they do
  for auth.
- `CardAuthorizationRequest` carries the new capture fields as nullable values
  until S1.3 wires acquisition. The NIBSS adapter must reject a request missing
  a mandatory field rather than guess one.

## 3. Tasks

### Capture model

- [x] Add `CardEntryMode` enum: `CONTACT`, `CONTACTLESS`, `MAGSTRIPE`,
      `FALLBACK_MAGSTRIPE`.
- [x] Add `CardholderVerificationMethod` enum: `ONLINE_PIN`, `OFFLINE_PIN`,
      `UNKNOWN`.
- [x] Add `EmvData` value object with hex validation and a redacted `toString`.
- [x] Add `PinBlockRef` value object: encrypted block, KSN and key id, no raw
      PIN.
- [x] Add `CardCapture` data class: masked PAN, entry mode, CVM, `CardToken`,
      `EmvData?`, `PinBlockRef?`, AID, application label, card serial, EMV result
      code, NFC data, expiry and service code.
- [x] Prohibit a `CardCapture` with a readable PAN: constructor guard plus unit
      tests (Luhn-valid PANs in the token, serial or label).

### Requests and results

- [x] Extend `CardAuthorizationRequest` with entry mode, CVM, EMV TLV, PIN block
      reference, AID, application label, NFC data and geo coordinates.
- [x] Keep `CardAuthorizationRequest` free of vendor or Android types.
- [x] Decide whether `PurchaseRequest` still carries card data. Decision: the
      use case acquires the card in S1.3; `PurchaseRequest` will carry amount,
      references, terminal and merchant only. Code change deferred.
- [ ] Update refund and reversal requests only if the host specification
      requires EMV data. Reversal now carries optional `MessageReasonCode` and
      `ReplacementAmounts`; EMV data stays out until NIBSS confirms.
- [x] Add `EmvHostResponse` (issuer authentication data and up to two issuer
      scripts) to `CardOperationResult` so the kernel can consume DE55 tags
      91/71/72.

### Errors

- [x] Add `CardCaptureErrorCode` for timeout, cancelled, card removed,
      unsupported card, PIN blocked, PIN timeout, terminal unavailable, printer
      unavailable and unreconciled.
- [x] Keep messages out of the domain: codes only, resolved to string resources
      in the UI.
- [ ] Add a vendor-code mapping table in the adapter layer. Deferred to the
      Kozen adapter; no vendor constants in the domain.

### NIBSS CTMS data the model must carry

The host interface requires values that the previous model did not have. See
[`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md).

- [x] Add `GeoCoordinates` (latitude, longitude, accuracy, capturedAt).
- [x] Add `NfcData` for contactless payloads (DE124).
- [x] Add `PinEntryCapability`, `TerminalOperator` and `TerminalTraits` for the
      DE22 PIN digit and the DE123 terminal positions.
- [x] Persist the exact host values needed to rebuild DE90: MTI, transmission
      date/time, acquirer ID and forwarding ID on `CardTransaction`.
- [x] Add `IssuerAuthenticationData`, `IssuerScript` and `EmvHostResponse`.
- [x] Add `MessageReasonCode` (`4000` cancellation, `4021` timeout).
- [x] Add `ReplacementAmounts` for partial and full reversals.
- [x] Keep processing-code selection (`00` purchase, `20` refund) out of the
      adapter; it belongs to the use case.
- [ ] Confirm whether PAN, track 2 and expiry are always available for magstripe,
      or whether chip transactions omit them.

## 4. Files

| Action | Path |
| --- | --- |
| Add | `domain/card/model/CardCapture.kt` |
| Add | `domain/card/model/CardEntryMode.kt` |
| Add | `domain/card/model/CardholderVerificationMethod.kt` |
| Add | `domain/card/model/EmvData.kt` |
| Add | `domain/card/model/EmvHostResponse.kt` |
| Add | `domain/card/model/PinBlockRef.kt` |
| Add | `domain/card/model/GeoCoordinates.kt` |
| Add | `domain/card/model/NfcData.kt` |
| Add | `domain/card/model/CardExpiry.kt` |
| Add | `domain/card/model/MessageReasonCode.kt` |
| Add | `domain/card/model/ReplacementAmounts.kt` |
| Add | `domain/card/model/CardCaptureErrorCode.kt` |
| Add | `domain/card/model/CardDataValidation.kt` (internal validation helpers) |
| Add | `domain/terminal/model/PinEntryCapability.kt` |
| Add | `domain/terminal/model/TerminalOperator.kt` |
| Add | `domain/terminal/model/TerminalTraits.kt` |
| Change | `domain/card/model/CardAuthorizationRequest.kt` |
| Change | `domain/card/model/CardOperationResult.kt` |
| Change | `domain/card/model/CardReversalRequest.kt` |
| Change | `domain/card/model/CardTransaction.kt` |
| Test | `app/src/test/java/.../domain/card/model/` |
| Test | `app/src/test/java/.../domain/terminal/model/TerminalTraitsTest.kt` |

## 5. Acceptance criteria

- A `CardCapture` cannot be constructed with a Luhn-valid PAN in the token,
  application label, card serial, EMV result code or service code.
- `toString()` of `CardToken`, `EmvData`, `PinBlockRef`, `NfcData` and
  `IssuerAuthenticationData` is redacted.
- No `android.*` or vendor import in the new domain files.
- Unit tests cover value validation, masking guard, error-code uniqueness and
  terminal traits.
- Full unit run passes: 155 tests.

## 6. Notes

- The Kozen SDK returns tracks and PAN in plaintext unless transaction
  encryption is enabled. Enabling encryption is an adapter responsibility; the
  domain guard is the second line of defence.
- `EmvData` and `NfcData` are redacted because EMV tags `5A`/`57` can carry PAN
  and track data even when the field as a whole leaks nothing else.
- Do not model scheme rules (floor limits, TAC decisions) in the domain yet.
  Those are certified configuration and change without code.

## 7. Deferred to later slices

| Item | Slice |
| --- | --- |
| `PurchaseRequest` carries no card data; use case acquires a `CardCapture` | S1.3 |
| `CardAuthorizationRequest` collapses the flat capture fields into `CardCapture` | S1.3 |
| Vendor error-code mapping (`CardReaderError` → `CardCaptureErrorCode`) | S5 |
| EMV data on refund/reversal if NIBSS requires it | S4b |
| Confirm magstripe PAN/track availability rules | S4b |
