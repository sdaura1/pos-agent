# Card Transaction Flow — Overview

Status: planned
Owner: TBD
Related: [`../ARCHITECTURE.md`](../../ARCHITECTURE.md)

## 1. Objective

Implement the complete card-acceptance flow: amount entry, card capture
(contact, contactless, magstripe), PIN entry, online authorisation, result,
receipt and history.

The flow must run today on an emulator against a fake terminal, and later run
on Kozen hardware by replacing one adapter. No business rule may live in the
vendor adapter.

## 2. Success criteria

- The purchase flow completes end to end with the fake terminal, covering
  approved, declined, timeout, cancel, card-removed and `UNKNOWN` outcomes.
- The domain layer has no `android.*`, Retrofit, Compose or `com.kozen.*`
  imports.
- No raw PAN is written to logs, storage, crash reports or analytics.
- Swapping `FakeCardAcceptor` for the Kozen implementation changes no code in
  `domain/` or `ui/`.
- A crash between persisting `INITIATED` and receiving a host response leaves a
  reconcilable record.

## 3. Scope

In scope:

- Card capture model and EMV interaction state machine.
- Ports: card acceptor, PIN pad, receipt printer, terminal identity/readiness.
- Fake terminal with scripted scenarios.
- Persistence, outbox events and reconciliation of `UNKNOWN`.
- NIBSS CTMS adapter: ISO 8583 codec, request/response mapping, reversals,
  key/parameter download and message hashing.
- Location capture for DE120 as part of the transaction path.
- Vendor-neutral receipt document and rendering rules.
- Card UI flow, view model and navigation.
- Vendor error-code mapping.

Out of scope:

- Real SDK binding, EMV parameter provisioning, L2/L3 certification, key
  injection, device permissions and whitelisting.
- Settlement, reporting, merchant onboarding.
- Secondary screen, keyboard/hotkey handling, ECR pairing.
- Multi-vendor abstraction beyond what one port needs.

## 4. Constraints

- Single `:app` module. Package boundaries enforce the layers until there is a
  reason to split modules.
- The terminal SDK is synchronous AIDL: methods return `int` status codes and
  callbacks arrive on binder threads. Coroutine/Flow conversion belongs in
  adapters.
- The EMV kernel interleaves acquisition with authorisation: the app receives
  an online-request callback and must reply with the host result.
- NIBSS CTMS speaks ISO 8583 over TCP: ASCII bitmap, 2-byte binary length
  prefix, and mandatory DE120 geo coordinates, DE123 POS data code and DE128
  message hash. See [`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md).
- Transactions cannot start until terminal keys (TSK) and EMV parameters have
  been downloaded or injected.
- The hardware and the three Kozen system services exist only on Kozen
  terminals. Everything must degrade gracefully elsewhere.
- Persistence must be crash-safe: record before any irreversible network call.

## 5. Architecture

```
        ui/card
           |
           v
  ProcessPurchaseUseCase ------------------+
           |                              |
           v                              v
   CardAcceptor (port)            NibssCardGateway (port)
           |                              |
   +-------+--------+                     |
   |                |                     |
FakeCardAcceptor  KozenCardAcceptor   NibssCardGatewayImpl
                                          |
                                     data/remote/nibss

  ProcessPurchaseUseCase also uses:
    CardTransactionRepository (port) -> data/local (Room)
    PinPad, ReceiptPrinter (ports)   -> fake or Kozen adapter
    TerminalProvisioning (port)      -> keys, parameters, readiness
```

The terminal acquires; NIBSS authorises. The use case owns the order of
operations and is the only place that knows both sides.

## 6. Purchase sequence

1. UI collects amount and starts `ProcessPurchaseUseCase`.
2. Use case starts `CardAcceptor`; the UI shows "present card".
3. Adapter prompts application selection, cardholder confirmation and PIN as
   required; the use case answers each prompt.
4. On the online prompt, the use case:
   a. obtains a location fix for DE120 and builds the ISO 8583 request,
   b. builds and persists the `CardTransaction` as `INITIATED` with the masked
      PAN,
   c. sends 0200 and waits for 0210 within the host deadline,
   d. replies to the kernel with the host result, including any issuer script or
      authentication data.
5. On the result prompt, the use case moves the aggregate to `APPROVED`,
   `DECLINED` or `UNKNOWN`, persists it with an outbox event and returns.
6. UI renders the result; receipt printing is triggered from the result screen
   (see [`07-receipts.md`](07-receipts.md)).
7. `UNKNOWN` records are picked up by reconciliation on next launch or on
   demand (see [`05-persistence.md`](05-persistence.md)).

If the user cancels or the card is removed before the online step, nothing is
persisted and the kernel is always stopped cleanly.

## 7. Slices

| Slice | Content | Effort | Depends on |
| --- | --- | --- | --- |
| S1 | Domain model, EMV session, ports, fakes, error mapping | 5–7 d | — |
| S2 | Persistence, outbox, reconciliation, use-case rewiring | 4–6 d | S1 |
| S3 | UI flow, PIN entry, receipts, history | 5–7 d | S1, S2 |
| S4 | NIBSS CTMS adapter, ISO 8583 codec, reversals, keys, fixtures | 8–12 d | S1 (credentials for completion) |

S4 is split into **S4a key exchange** (see
[`13-key-exchange.md`](13-key-exchange.md), in progress) and **S4b transaction
adapter** (see [`08-nibss-adapter.md`](08-nibss-adapter.md)).
| S5 | Kozen adapter and device bring-up | Blocked | Hardware, firmware, cert, keys |
| S6 | Hardening: accessibility, telemetry, PII audit | 2–4 d | S3, S4 |

S1 retires the most risk and starts first.

## 8. Glossary

| Term | Meaning |
| --- | --- |
| Capture | Data produced by the terminal for one card presentation |
| Prompt / reply | One step in the EMV dialogue: the adapter asks, the app answers |
| Entry mode | Contact, contactless, magstripe or fallback |
| CVM | Cardholder verification method (online PIN, signature, none) |
| DE 55 | ISO 8583 field carrying EMV TLV data |
| KSN | Key serial number for DUKPT key derivation |
| `UNKNOWN` | Host outcome not known: may have been approved, must be reconciled |

## 9. Related documents

- [`02-domain-model.md`](02-domain-model.md)
- [`03-emv-session.md`](03-emv-session.md)
- [`04-ports-and-fakes.md`](04-ports-and-fakes.md)
- [`05-persistence.md`](05-persistence.md)
- [`06-ui-flow.md`](06-ui-flow.md)
- [`07-receipts.md`](07-receipts.md)
- [`08-nibss-adapter.md`](08-nibss-adapter.md)
- [`09-kozen-adapter.md`](09-kozen-adapter.md)
- [`10-testing-strategy.md`](10-testing-strategy.md)
- [`11-risks-and-decisions.md`](11-risks-and-decisions.md)
- [`12-nibss-ctms-spec.md`](12-nibss-ctms-spec.md)
