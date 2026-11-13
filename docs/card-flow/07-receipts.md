# S3.2 — Receipts

Status: planned
Slice: S3
Depends on: S2 aggregates
Effort: 2–3 developer-days
Related: [`06-ui-flow.md`](06-ui-flow.md)

## 1. Goal

Produce a vendor-neutral receipt document from a completed transaction and
render it later on the Kozen printer without changing the content rules.

## 2. Why a document model

Receipt content (what is printed) is business logic. Printer commands (how it is
printed) are hardware. Separating them enables golden tests on the JVM, a
preview on the result screen, reprints and a future second printer without
touching the domain.

## 3. Document model

`domain/receipt/model`:

| Type | Purpose |
| --- | --- |
| `Receipt` | ordered list of blocks plus metadata (transaction id, reference, copy type) |
| `ReceiptBlock` | sealed: header, text line, divider, spacer, barcode, QR, cut |
| `ReceiptLine` | text, alignment, emphasis, double height/width |
| `ReceiptCopy` | `MERCHANT` or `CUSTOMER` |

Formatting is expressed as intent, never as escape codes or column padding for a
specific paper width.

## 4. Content specification

Every approved receipt:

1. Merchant name and address, from merchant configuration.
2. Terminal ID and merchant ID.
3. Date and time in the merchant's locale, ISO reference.
4. Amount, formatted from `Money` with the transaction currency.
5. Masked PAN and entry mode.
6. Auth code, RRN and response code when present.
7. "APPROVED" banner.
8. Signature line when CVM is signature.
9. Customer copy marker on the customer copy.
10. Reprint marker on a reprint, with the original timestamp.

Declined and unknown receipts carry the status banner and reason code, with no
auth code. Unknown receipts say the outcome is being confirmed.

Rules:

- Never print a full PAN, track data, PIN block or KSN.
- Amount formatting comes from `Money` and one formatter, not from string
  concatenation at the call site.
- Line width is a renderer concern; the document may contain logical lines that
  the renderer wraps.
- Content is deterministic: same transaction and copy type produce an identical
  document. This is what makes golden tests possible.

## 5. Rendering

- Fake renderer: returns a text preview used by the result screen and tests.
- Kozen renderer (S5): maps blocks to `TextPrintLine`, `BarcodePrintLine`,
  `BitmapPrintLine`, opens the printer, prints, handles out-of-paper and closes.
- A print failure never changes the transaction status; it is surfaced to the
  cashier with a retry.

## 6. Tasks

- [ ] Add the document model and `ReceiptBuilder` that maps a `CardTransaction`
      plus merchant/terminal config to a `Receipt`.
- [ ] Add the amount and date formatters used by the builder.
- [ ] Add golden tests for approved, declined, unknown, customer copy and
      reprint.
- [ ] Add the fake renderer and result-screen preview.
- [ ] Add a redaction test that fails if any block contains 13–19 consecutive
      digits.
- [ ] Defer the Kozen renderer to S5.

## 7. Files

| Action | Path |
| --- | --- |
| Add | `domain/receipt/model/Receipt.kt` |
| Add | `domain/receipt/model/ReceiptBlock.kt` |
| Add | `domain/receipt/model/ReceiptCopy.kt` |
| Add | `domain/receipt/ReceiptBuilder.kt` |
| Add | `data/device/fake/FakeReceiptRenderer.kt` |
| Test | `app/src/test/java/.../domain/receipt/` |

## 8. Acceptance criteria

- Golden files exist for the five receipt variants and are reviewed.
- A PAN-shaped value can never pass the redaction test.
- The same transaction always produces an identical document.
- Result screen shows the preview without any printer dependency.
