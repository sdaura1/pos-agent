# S1.3 — Ports and Fake Terminal

Status: planned
Slice: S1
Depends on: [`02-domain-model.md`](02-domain-model.md)
Effort: 2–3 developer-days
Related: [`03-emv-session.md`](03-emv-session.md), [`06-ui-flow.md`](06-ui-flow.md)

## 1. Goal

Define the narrow ports the app depends on, and ship fakes complete enough to
run the whole flow on an emulator, in UI tests and in CI.

## 2. Ports

All ports are interfaces in `domain/card/port` (readiness/identity may live in
`domain/terminal/port` if they are reused by other contexts).

### CardAcceptor

Runs one acquisition session. The adapter drives the EMV dialogue and suspends
on every prompt until the app answers.

```kotlin
interface CardAcceptor {
    suspend fun acquire(
        request: AcquisitionRequest,
        respond: suspend (EmvPrompt) -> EmvReply,
    ): Response<AcquisitionResult>

    suspend fun cancel()
}
```

`AcquisitionRequest` carries amount, transaction type, timeout and a flag for
allowed entry modes. `AcquisitionResult` is a `CardCapture` or a terminal
outcome (cancelled, timed out, failed).

### PinPad

Collects an encrypted PIN. Separate from `CardAcceptor` because the PIN UI is
app-owned (the SDK renders into app-supplied views) and because PIN entry must
be testable on its own.

```kotlin
interface PinPad {
    suspend fun collect(request: PinRequest): Response<PinBlockRef>
    fun cancel()
}
```

### ReceiptPrinter

```kotlin
interface ReceiptPrinter {
    suspend fun print(document: Receipt): Response<Unit>
    suspend fun status(): PrinterStatus
}
```

The port takes the vendor-neutral `Receipt` from
[`07-receipts.md`](07-receipts.md). Rendering to printer commands is an adapter
concern.

### TerminalIdentity

```kotlin
interface TerminalIdentity {
    suspend fun serialNumber(): String?
    suspend fun tusn(): String?
    suspend fun csn(): String?
    suspend fun emvKernelVersion(): String?
    suspend fun financialServiceVersion(): String?
}
```

### TerminalProvisioning

Implemented ahead of the rest of S1 (see
[`13-key-exchange.md`](13-key-exchange.md)).

```kotlin
interface TerminalProvisioning {
    val state: StateFlow<ProvisioningState>
    suspend fun ensureProvisioned(): ProvisioningResult
    suspend fun reprovision(): ProvisioningResult
}
```

States: `Unprovisioned`, `DownloadingKeys`, `DownloadingParameters`, `Provisioned`,
`Failed(reason, detail)`. `ProvisioningFailureReason` covers service unavailable,
version mismatch, permission denied, key download, parameter download, hash not
configured, unsupported device and unknown. `EnsureTerminalProvisionedUseCase` maps
failures to stable `ProvisioningErrorCode` values.

`Provisioned` is reachable only through a completed parameter download. The purchase
flow must call the use case before acquisition and disable the start action
otherwise. A production placeholder reports `SERVICE_UNAVAILABLE` until the
Kozen adapter is installed.

## 3. Fakes

Location: `data/device/fake`. All fakes are deterministic and driven by an
explicit scenario, never by time or randomness.

### FakeCardAcceptor

- `scenario` selects the scripted dialogue.
- Emits prompts from a script and records replies for assertions.
- Honours `cancel()` by answering the pending prompt then ending.
- Can fail before the first prompt, after capture, or at the online step.
- Exposes the list of prompts and replies for contract tests.

### FakePinPad

Modes: `SUCCESS` (returns a fixed, obviously fake block), `CANCELLED`,
`BLOCKED`, `TIMEOUT`. Returns a `PinBlockRef` whose KSN is marked as test data
so it can never be mistaken for a live value.

### FakeReceiptPrinter

Captures the document and exposes it for assertions. Modes: `SUCCESS`,
`OUT_OF_PAPER`, `OFFLINE`.

### FakeTerminalIdentity / FakeTerminalProvisioning

Stable fake identifiers and a deterministic provisioning double. The fake
defaults to `Provisioned`; pass a failure outcome to test the unavailable paths.
`ensureProvisioned()` is single-flight and `reprovision()` always performs a new attempt.

## 4. Scenario matrix

| Scenario | Entry mode | Prompts | Expected result | Persisted |
| --- | --- | --- | --- | --- |
| Approved contact | contact | select, confirm, PIN, online, finished | `APPROVED` | yes, + outbox |
| Approved contactless | contactless | online, finished | `APPROVED` | yes, + outbox |
| Declined by host | contact | …, online, finished | `DECLINED` | yes |
| Host timeout | contact | …, online, finished | `UNKNOWN` | yes, reconcile |
| Application selection | contact | select (2 apps) | `APPROVED` or `DECLINED` | yes |
| User cancel at card wait | — | none | `Cancelled` | no |
| User cancel during PIN | contact | …, PIN | `Cancelled` | no |
| Card removed mid-session | contact | … | `Failed(card removed)` | no |
| PIN blocked | contact | …, PIN | `Failed(pin blocked)` | no |
| Printer offline | any approved | … | `APPROVED`, print fails | yes |
| Terminal not provisioned | — | none | `Unavailable` before start | no |
| Terminal unavailable | — | none | `Unavailable` before start | no |

## 5. Tasks

- [x] Add the `TerminalProvisioning` port, state machine, gate and fake.
- [ ] Add the remaining port interfaces and their request/result types.
- [ ] Implement `FakeCardAcceptor` with the scenario scripts above.
- [ ] Implement `FakePinPad`, `FakeReceiptPrinter`, `FakeTerminalIdentity`.
- [ ] Add an adapter contract test suite that both the fake and the future Kozen
      adapter must pass (see [`10-testing-strategy.md`](10-testing-strategy.md)).
- [ ] Bind fakes in Hilt for debug and test builds only; production binding
      stays unset until S5.

## 6. Files

| Action | Path |
| --- | --- |
| Add | `domain/card/port/CardAcceptor.kt` |
| Add | `domain/card/port/PinPad.kt` |
| Add | `domain/card/port/ReceiptPrinter.kt` |
| Add | `domain/card/port/AcquisitionRequest.kt` |
| Add | `domain/terminal/port/TerminalIdentity.kt` |
| Add | `domain/terminal/port/TerminalProvisioning.kt` |
| Add | `domain/terminal/model/Provisioning*.kt` |
| Add | `domain/terminal/usecase/EnsureTerminalProvisionedUseCase.kt` |
| Add | `data/device/provisioning/UnavailableTerminalProvisioning.kt` |
| Add | `data/device/fake/FakeCardAcceptor.kt` |
| Add | `data/device/fake/FakePinPad.kt` |
| Add | `data/device/fake/FakeReceiptPrinter.kt` |
| Add | `data/device/fake/FakeTerminal*.kt` |
| Change | `di/` (fake bindings, debug/test only) |

## 7. Acceptance criteria

- The fake terminal can complete every scenario in the matrix in a JVM test.
- Cancellation always answers the pending prompt; the contract suite asserts it.
- Fakes never use real time; tests are deterministic and fast.
- Domain ports expose no Android or vendor types.
- The production dependency graph can build without the Kozen AARs.
