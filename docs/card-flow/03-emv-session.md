# S1.2 — EMV Session State Machine

Status: planned
Slice: S1
Depends on: [`02-domain-model.md`](02-domain-model.md)
Effort: 3–4 developer-days
Related: [`04-ports-and-fakes.md`](04-ports-and-fakes.md)

## 1. Goal

Model the EMV dialogue as a pure Kotlin state machine so the entire chip and
contactless flow can be built, demonstrated and tested without hardware. The
vendor adapter later becomes a translator: vendor callback in, prompt out,
reply back.

## 2. Why this matters

The kernel is a state machine that the app drives. For contact and contactless
it asks the app to:

- choose an application when several AIDs match,
- confirm cardholder data or CVM,
- collect a PIN,
- perform the online authorisation and return the host result.

Getting the ordering, cancellation and "reply exactly once" rules wrong leaves
the kernel stuck or the card powered on. Those rules are business logic and must
not live in the adapter.

## 3. States

| State | Meaning | Exits |
| --- | --- | --- |
| `Idle` | No session | `start` |
| `WaitingForCard` | Present card, countdown running | card presented, timeout, cancel |
| `ApplicationSelection` | Several applications matched | reply select, cancel, failure |
| `CardholderConfirmation` | CVM or amount confirmation | reply confirm, cancel |
| `PinEntry` | PIN required | PIN block, PIN cancel, timeout |
| `Online` | Host authorisation in flight | host result, cancel |
| `SecondTap` | Contactless re-tap requested | card presented, timeout |
| `Completed` | Capture produced | terminal |
| `Declined` | Kernel declined | terminal |
| `Cancelled` | User or app cancelled | terminal |
| `TimedOut` | Card or PIN timeout | terminal |
| `Failed` | Error or card removed | terminal |

Only one state is active. The machine rejects transitions that are not in the
declared table.

## 4. Prompts and replies

The adapter asks; the app answers. Every prompt must be answered exactly once,
including on cancellation.

| Prompt | Payload | Allowed replies |
| --- | --- | --- |
| `CardPresented` | entry mode, ATR/serial if known | none (informational) |
| `SelectApplication` | list of application labels, first-select flag | `Select(index)` or `Cancel` |
| `ConfirmCardInfo` | reason (amount, CVM, issuer referral, GPO filter), card info | `Accept` or `Decline` or `Cancel` |
| `RequestPin` | PIN mode, amount, attempts remaining | `PinEntered(PinBlockRef)` or `PinCancelled` or `PinTimeout` |
| `RequestOnline` | capture draft: masked PAN, entry mode, CVM, EMV TLV, KSN | `HostApproved`, `HostDeclined`, `HostUnknown` |
| `SecondTap` | reason | none (informational) |
| `KernelSelected` | scheme | none (informational) |
| `Finished` | result code, capture | none (terminal) |
| `Failure` | error code, message | none (terminal) |

`HostUnknown` is not a failure. It means the host response was not received and
the transaction must be marked `UNKNOWN` and reconciled.

## 5. Rules

- **Reply once.** A reply to an already-answered prompt is rejected and logged
  in debug builds only.
- **Cancel answers the pending prompt.** Cancellation never abandons a prompt;
  it answers with `Cancel` and then transitions to `Cancelled`.
- **Timeouts are explicit.** Card wait uses the session timeout. PIN uses the
  PIN pad timeout. There is no silent timeout during `Online`; instead the host
  call has its own deadline that maps to `HostUnknown`.
- **Second tap** may occur at most twice; after that the session fails with a
  card-removed error.
- **Card removal** before `Finished` transitions to `Failed` with a
  card-removed code and no capture is produced.
- **Terminal states** produce exactly one result object and unregister any
  listener.
- **No persistence in the machine.** It produces a capture or an error; the use
  case decides what to store and when.

## 6. Sequencing notes

- The online prompt is the only prompt that causes a network call. The machine
  does not know about NIBSS; it only sees the host result.
- The machine must emit `RequestOnline` only after the kernel has produced all
  data needed for DE 55 and the masked PAN.
- If the host call outlives the screen, the session continues; the UI
  reattaches. Do not tie the state machine to an Activity lifecycle.

## 7. Mapping to the Kozen kernel (reference for S5)

| Vendor callback | Prompt |
| --- | --- |
| `onEmvProcess` | `CardPresented` |
| `onSelectApplication` | `SelectApplication` |
| `onConfirmCardInfo` | `ConfirmCardInfo` |
| `onKernelType` | `KernelSelected` |
| `onSecondTapCard` | `SecondTap` |
| `onRequestInputPin` | `RequestPin` |
| `onRequestOnlineProcess` | `RequestOnline` |
| `onTransactionResult` | `Finished` or `Failure` |
| returned status code | `Failure` when non-zero and no result |

Replies map to `setSelectApplicationResponse`, `setCardInfoResponse`,
`setPinResponse` and `setOnlineResponse`. This mapping lives in
`data/device/kozen` and nowhere else.

## 8. Tasks

- [ ] Add sealed `EmvState`, `EmvPrompt`, `EmvReply`, `AcquisitionResult`.
- [ ] Implement the transition table with explicit validation and a rejected
      transition error.
- [ ] Implement reply-once enforcement and pending-prompt tracking.
- [ ] Implement cancel, card-removed, session timeout, PIN timeout and
      second-tap limits.
- [ ] Model the capture draft handed to `RequestOnline` and the final
      `CardCapture`.
- [ ] Expose a suspend entry point used by `CardAcceptor`, with no Android
      dependency.
- [ ] Add a transition-matrix unit test and a property test that no sequence of
      random prompts can reach an illegal state.
- [ ] Add a test that cancellation at each state answers the pending prompt and
      ends in a terminal state.

## 9. Files

| Action | Path |
| --- | --- |
| Add | `domain/card/emv/EmvState.kt` |
| Add | `domain/card/emv/EmvPrompt.kt` |
| Add | `domain/card/emv/EmvReply.kt` |
| Add | `domain/card/emv/EmvSession.kt` |
| Add | `domain/card/emv/AcquisitionResult.kt` |
| Test | `app/src/test/java/.../domain/card/emv/` |

## 10. Acceptance criteria

- All transitions covered by tests, including illegal ones.
- Cancel at every non-terminal state leaves the machine terminal and no capture.
- Session timeout, PIN timeout, second-tap limit and card removal behave as
  specified.
- No `android.*` or vendor type in the package.
- The fake adapter can drive a full approved and a full declined session.
