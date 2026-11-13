# Testing Strategy

Status: planned
Applies to: S1–S6
Related: [`04-ports-and-fakes.md`](04-ports-and-fakes.md), [`09-kozen-adapter.md`](09-kozen-adapter.md)

## 1. Principles

- The domain is plain Kotlin and is tested on the JVM.
- Every port has a fake; the UI and use cases never depend on the vendor SDK.
- One contract suite is run against the fake and, later, the Kozen adapter.
- Tests are deterministic: no real time, no randomness, no network.
- Card secrets are absent from fixtures except deliberately fake values that are
  structurally invalid as live data.

## 1.1 Test style

- Every test name follows
  `` `<action> should <expected outcome> when <condition>` `` in backticks.
- Every test body is split by `// Arrange`, `// Act`, `// Assert`, with a blank
  line between the sections.
- Every line, including the full `fun` declaration, is under 100 characters.
- One behaviour per test; assertions appear only in the Assert section.
- No sleeps, real time or network in unit tests.
- The full convention lives in `.opencode/skills/pos-agent-testing/SKILL.md`.

## 2. Layers

| Layer | Tooling | Covers |
| --- | --- | --- |
| Value objects and aggregate | JUnit | validation, masking guard, status transitions |
| EMV session | JUnit | transition table, reply-once, cancel, timeouts |
| Use cases | JUnit + fakes | purchase orchestration, refund ceilings, reconciliation |
| Repository | Room in-memory | atomic writes, queries, crash injection |
| Mappers | JUnit | host codes, vendor codes, enums, amounts |
| Receipts | JUnit goldens | content, copies, reprints, redaction |
| UI | Compose test | states, navigation, cancel/back, readiness |
| Adapter contract | JUnit | identical behaviour fake vs real |
| NIBSS adapter | fake server / recorded frames | approve, decline, timeout, duplicate, bad hash, reversal |

## 3. Scenario matrix

The matrix in [`04-ports-and-fakes.md`](04-ports-and-fakes.md) is the shared
source of truth. Every row must exist at three levels: session unit test, use
case test and UI test (where the UI is involved).

Additional cases:

- Double start (user taps twice).
- Process death between persist and authorise.
- Reconciliation when the host returns approved, declined and still-unknown.
- Refund above the original amount; refund of a non-approved purchase.
- Reversal of an approved purchase; reversal of an unknown purchase.
- Receipt print failure after an approved transaction.
- Terminal becomes unavailable between amount entry and card presentation.
- ISO 8583 vectors: pack/unpack round-trip for 0200, 0210, 0420, 0430, 0800,
  0810, including secondary bitmap, ASCII bitmap and 2-byte length framing.
- DE128 hash: known-answer test once NIBSS supplies the composition; mismatch
  must never be treated as approval.
- Reversal: full and partial replacement amounts, DE90 reconstruction, DE56
  reasons `4000` and `4021`, repeat and `0430` handling.
- Response codes: `00`, a decline, `68`, `95`, and an unknown code.

## 4. Contract suite

`CardAcceptorContract` asserts:

- prompts arrive in a legal order,
- every prompt is answered exactly once,
- `cancel()` always answers and terminates,
- success yields a capture with a masked PAN and no readable PAN,
- failure yields a domain error code, not a vendor code.

`NibssCardGatewayContract` asserts:

- duplicate reference does not double-authorise,
- timeout maps to `UNKNOWN`,
- response codes map to the expected statuses,
- no secret appears in any captured log.

## 5. Quality gates

- Domain unit test coverage at or above 90 percent; no coverage target for
  generated code.
- An architecture test fails the build if `domain/` imports `android.*`,
  `androidx.*`, Retrofit, Compose or `com.kozen.*`.
- A secret-scan test fails if any fixture, golden or log capture contains a
  PAN-shaped value, track data or a PIN block.
- Deterministic tests only: a test that needs a clock gets `Clock` injected.
- All new UI states have a preview and a string resource.

## 6. Fixtures

- Capture real vendor bundles on device during S5 and store them as JSON
  fixtures; treat them as sensitive and redact PAN, tracks and KSN.
- Store NIBSS requests/responses as redacted fixtures.
- Receipt goldens are plain text, reviewed in code review.

## 7. CI

- Unit, repository and UI tests run on every change.
- The `mock` flavor builds on every change; the `kozen` flavor builds on
  changes that touch the adapter.
- Contract suites run on the JVM; device suites are a manual or nightly job with
  hardware attached.

## 8. Manual device testing

Defined in [`09-kozen-adapter.md`](09-kozen-adapter.md), section 9. A release
candidate is not accepted until that checklist passes on the target firmware.
