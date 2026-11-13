# S4 — NIBSS CTMS Adapter (ISO 8583)

Status: partially blocked (credentials, hash detail, certification scripts)
Slice: S4
Depends on: S1 model and ports
Effort: 8–12 developer-days
Spec digest: [`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md)
Related: [`05-persistence.md`](05-persistence.md), [`11-risks-and-decisions.md`](11-risks-and-decisions.md)

## 1. Goal

Implement `NibssCardGateway` against the NIBSS CTMS interface: ISO 8583 over TCP,
ASCII bitmap, a 2-byte binary length prefix, and the message set in the spec digest.

The transport decision is settled by the specification: **ISO 8583 over TCP**, not
REST. `jpos` is already a dependency and owns packing, parsing and bitmap work.

## 2. Architecture

```
NibssCardGatewayImpl
  ├── NibssConnection         TCP, framing, reconnect, correlation
  ├── Iso8583Codec            jpos packager, ASCII bitmap, length header
  ├── Iso8583MessageBuilder   domain request -> ISO message (DE by DE)
  ├── Iso8583MessageParser    ISO message -> domain result
  ├── MessageHasher           SHA-256 over the message, seeded with TSK
  ├── SecretKeyStore          TMK/TPK/TSK/IPEK, encrypted at rest
  ├── GeoProvider             DE120 from the terminal location module
  └── ResponseCodeMapper      DE39 -> TransactionStatus
```

The adapter is the only place that knows field numbers, encodings, MTIs or response
codes. The domain sees `CardAuthorizationRequest` and `CardOperationResult`.

## 3. Messages

| Operation | MTI | Processing code | Notes |
| --- | --- | --- | --- |
| Purchase | 0200 → 0210 | `00` | Financial request |
| Purchase repeat | 0201 → 0210 | `00` | Only if the request provably did not reach the host |
| Refund | 0200 → 0210 | `20` | Same layout as purchase |
| Balance inquiry | 0100 → 0110 | `31` | If in scope |
| Reversal | 0420/0421 → 0430 | `00` (original) | Advice; DE90, DE95, DE56 required |
| Sign-on / echo | 0800 → 0810 | `9D` or network code | Confirm exact code with NIBSS |
| Key download | 0800 → 0810 | `9A`, `9B`, `9G`, `9I`, `9J` | Prerequisite for transactions |
| Parameter download | 0800 → 0810 | `9C`, `9E`, `9F` | AIDs, CA keys, terminal params |

All layout details and mandatory fields are in
[`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md), section 3.

## 4. Request mapping

| Domain | DE | Rule |
| --- | --- | --- |
| `maskedPan` | — | Display only; the host PAN comes from the encrypted capture |
| `CardToken` | 35 | Encrypted track-2 equivalent, or omitted for chip-only |
| `EmvData` | 55 | TLV, required tags per spec section 8 |
| `PinBlockRef` | 52 | Hex16 PIN block; DE26 PIN capture length |
| capture entry mode | 22 | Map per spec section 6, append PIN capability digit |
| capture CVM / entry mode | 123 | Assemble all 15 positions per spec section 13 |
| `amount` | 4 | Minor units, `n12` |
| currency | 49 | `566` |
| `stan` | 11 | `n6` |
| `reference` | 37 | `an12`; idempotency key |
| terminal / merchant | 41 / 42 / 43 | TID, MID, name/location |
| local time/date | 12 / 13 | `hhmmss`, `MMDD` |
| transmission date/time | 7 | `MMDDhhmmss` UTC |
| expiry | 14 | `YYMM` |
| MCC | 18 | From merchant config |
| acquirer / forwarding | 32 / 33 | From terminal config |
| geo | 120 | `01<len>lat02<len>lon`, mandatory |
| NFC payload | 124 | When present |
| echo | 59 | Optional correlation value |

`CardAuthorizationRequest` therefore needs the capture metadata listed in
[`02-domain-model.md`](02-domain-model.md).

## 5. Response handling

| DE | Field | Use |
| --- | --- | --- |
| 39 | Response code | `00` approved; everything else declined/referral |
| 38 | Authorization code | Persist and print |
| 37 | RRN | Must match the request; persist |
| 54 | Approved amounts | Confirm the approved amount |
| 55 | Issuer auth data and scripts | Feed back to the EMV kernel (tags 91/71/72) |
| 62 | CTMS date/time, timeout | Operational data |
| 59 | Echo data | Verify it matches the request |
| 128 | Message hash | Verify before trusting the message |

Mapping:

| Condition | Result |
| --- | --- |
| DE39 = `00` | `APPROVED` |
| DE39 = `68` or `95` | `UNKNOWN`, reconcile |
| DE39 = any other | `DECLINED` with the code |
| No response within the deadline | `UNKNOWN` |
| Connection lost before send | Retry once, then `DECLINED`/retryable error |
| Connection lost after send | `UNKNOWN` |
| Invalid hash or malformed message | Reject; do not treat as approval |

## 6. Reversals and timeouts

- A host timeout is **not** a decline. Send `0420` with DE56 `4021`, DE90 from the
  original request, and zeroed DE95. Then mark the transaction `UNKNOWN`.
- A cashier cancellation after the host may have seen the request uses DE56 `4000`.
- A partial reversal carries the corrected amount in DE95.
- Repeats use `0421`. Bound the number of repeats and back off between them.
- A `0430` response resolves the reversal; it does not by itself prove the original
  was declined.
- Never re-authorise an `UNKNOWN` transaction with a new reference.

## 7. Hash and keys

- Compute DE128 (or DE64 when no secondary fields) as SHA-256, seeded with the TSK.
- Verify the hash on every response before parsing business fields.
- TSK and the other keys are obtained through 0800 key download at start-up, or
  injected per the acquirer's procedure.
- Keys are stored encrypted at rest, never in plain preferences, never logged.
- Transactions are blocked until a TSK and terminal parameters exist.
- The exact hash composition is an open question (spec section 16); implement it behind
  `MessageHasher` so only that class changes when NIBSS clarifies.

## 8. Connection and framing

- Persistent TCP socket, 2-byte binary length prefix, ASCII payload.
- One in-flight request per terminal unless NIBSS confirms otherwise; correlate by
  STAN and reference.
- Reconnect with backoff. On reconnect, re-run sign-on and confirm keys are still valid.
- The host-advertised deadline comes from DE62 tag 04 (the spec example is 2 seconds);
  derive the client timeout from it, clamped to a configured maximum.
- A single writer and reader coroutine per socket; no blocking I/O on the main thread.

## 9. Geo coordinates

- DE120 is mandatory on every request and reversal.
- Obtain a fix before starting a transaction; if no fix is available, apply the agreed
  policy: cached fix with timestamp, or block the sale. Record which was used.
- Format exactly as the spec: `01` + 2-digit length + latitude, `02` + 2-digit length +
  longitude, minimum five decimal places.

## 10. Security and logging

- TLS is not specified because this is a private TCP link; confirm with NIBSS whether
  the link is a VPN or leased line.
- Never log PAN, track data, PIN block, KSN or keys.
- Log MTI, STAN, RRN, response code and timing only.
- Debug logging of raw messages is compiled out of release builds and redacted in
  debug.

## 11. Tasks

- [ ] Obtain test host, credentials, certification scripts and the hash
      specification; record answers to spec section 16.
- [ ] Add the jpos packager definition for the CTMS dialect: ASCII bitmap, 2-byte
      binary length header, field formats per the digest.
- [ ] Implement framing, connection management, correlation and reconnect.
- [ ] Implement `Iso8583MessageBuilder` for 0200, 0420/0421 and 0800.
- [ ] Implement `Iso8583MessageParser` for 0210, 0430 and 0810, including DE38/DE39 on
      0210 if NIBSS confirms the layout defect.
- [ ] Implement `ResponseCodeMapper` as a table-driven map.
- [ ] Implement `MessageHasher` and verify response hashes.
- [ ] Implement `SecretKeyStore` and the 0800 key/parameter download flow.
- [ ] Implement DE120 geo assembly and the no-fix policy.
- [ ] Implement DE123 assembly and DE22 mapping from the capture.
- [ ] Implement DE55 request assembly and response tag extraction (91/71/72).
- [ ] Implement reversal and timeout policy.
- [ ] Add fixtures for approved, declined, timeout, duplicate, bad hash, DE39 68/95
      and reversal success/failure.
- [ ] Add the gateway contract test shared with the fake gateway.
- [ ] Wire `di/NetworkModule` with host, port and timeouts from build config.

## 12. Files

| Action | Path |
| --- | --- |
| Add | `data/remote/nibss/iso/NibssConnection.kt` |
| Add | `data/remote/nibss/iso/Iso8583Codec.kt` |
| Add | `data/remote/nibss/iso/Iso8583MessageBuilder.kt` |
| Add | `data/remote/nibss/iso/Iso8583MessageParser.kt` |
| Add | `data/remote/nibss/iso/hash/MessageHasher.kt` |
| Add | `domain/terminal/port/SecretKeyStore.kt` |
| Add | `data/remote/nibss/iso/GeoProvider.kt` |
| Add | `data/remote/nibss/iso/ResponseCodeMapper.kt` |
| Add | `data/remote/nibss/NibssCardGatewayImpl.kt` |
| Change | `app/src/main/resources/jpos.xml` |
| Change | `di/NetworkModule.kt` |
| Test | `app/src/test/java/.../data/remote/nibss/` |

## 13. Acceptance criteria

- A purchase round-trip against the test host produces `APPROVED` with RRN and auth
  code persisted and printed.
- A decline preserves DE39 and is displayed as a decline, not an error.
- A timeout produces `UNKNOWN` plus a `0420` reversal and a reconcilable record.
- Response hash verification failure is rejected and never surfaced as approval.
- No card secret appears in logs or fixtures.
- The same contract suite passes against the fake gateway and the real adapter.
