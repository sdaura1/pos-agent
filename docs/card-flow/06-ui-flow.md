# S3.1 — Card UI Flow

Status: planned
Slice: S3
Depends on: S1, S2
Effort: 4–5 developer-days
Related: [`03-emv-session.md`](03-emv-session.md), [`07-receipts.md`](07-receipts.md)

## 1. Goal

Build the purchase flow UI so a cashier can take a payment from amount entry to
result, including PIN entry, cancellation and the terminal-unavailable state.

`AmountScreen` is currently a placeholder and there is no card graph.

## 2. Routes

Add to `MainGraphRoute`:

| Route | Screen | Purpose |
| --- | --- | --- |
| `Amount` | amount entry | exists, replace placeholder |
| `PresentCard` | card presentation | entry-mode hints, countdown, cancel |
| `CardPin` | PIN entry | modal over the card screen |
| `CardProcessing` | online | indeterminate progress, no cancel |
| `CardResult` | result | approved/declined/unknown, print, done |
| `TransactionHistory` | history | exists, wire to data |
| `TransactionDetail` | detail | add, including reconcile action |

The signed-in graph keeps the dashboard as start; the card graph is entered from
it.

## 3. View model

One `CardFlowViewModel` with a single sealed UI state. It owns:

- the current amount and references,
- the active prompt from the state machine,
- PIN request state,
- readiness state from `TerminalProvisioning`,
- the final result.

It does **not** own the state machine instance across process death; a session
lost with the process is abandoned and any persisted `INITIATED` row is
reconciled.

State sketch:

| State | UI |
| --- | --- |
| `Amount` | keypad, amount display, start button |
| `WaitingForCard` | tap/insert/swipe hints, countdown, cancel |
| `SelectApplication` | application list |
| `Confirm` | amount/CVM confirmation |
| `Pin` | PIN entry modal |
| `SecondTap` | "present card again" |
| `Processing` | spinner, no cancel |
| `Result` | status, masked PAN, amount, reference |
| `Unavailable` | why the terminal cannot be used, retry |
| `Failed` | mapped error, retry or back |

## 4. PIN entry

This is the one place where the vendor SDK dictates the UI shape: the Kozen PIN
pad takes app-supplied `View`s keyed by `PinViewEnum` (digits, backspace, enter,
escape) and the SDK reads them. Therefore:

- Keep the PIN surface in one place, `ui/card/pin`, behind the `PinPad` port.
- In the fake implementation, provide a normal Compose keypad.
- In the Kozen implementation, host an `AndroidView` with the required view IDs
  and hand them to the SDK.
- Never cache PIN views, never read their contents, never log them.
- Set `FLAG_SECURE` on the window while PIN entry is visible.
- Support hardware keypad and accessibility labels; the vendor SDK also
  supports a physical keyboard, so both paths must be tested.

## 5. Interaction rules

- **Cancel** is confirmed before it aborts. It must answer the pending prompt
  and stop the kernel; never navigate away leaving a live session.
- **Back** follows the same rule as cancel, except during `Processing` where it
  is ignored.
- **Duplicate taps** on start are ignored while a session is active.
- **Readiness** is checked before starting; if unavailable, the start button is
  disabled with the reason shown.
- **Result screen** offers print and done. A failed print does not change the
  transaction status.
- **UNKNOWN** is displayed as "outcome to be confirmed", not as failure, with a
  reconcile action.
- All user-facing text comes from string resources; domain codes are mapped in
  the UI layer, as with auth errors.

## 6. Accessibility and i18n

- Amount keypad and PIN pad expose content descriptions and support TalkBack.
- Countdowns announce changes at most every 5 seconds.
- Result status is announced on entry.
- No text baked into the view model or state machine.
- Dynamic type and landscape layouts verified on the smallest supported device.

## 7. Tasks

- [ ] Replace the placeholder `AmountScreen` with real entry and validation.
- [ ] Add routes and wire them into `MainNavGraph`.
- [ ] Implement `CardFlowViewModel` and its UI state.
- [ ] Implement `PresentCardScreen`, `CardProcessingScreen`, `CardResultScreen`.
- [ ] Implement the PIN surface behind `PinPad`, with fake and Compose versions.
- [ ] Implement cancel/back handling that always answers the state machine.
- [ ] Implement the unavailable-terminal state.
- [ ] Wire history and detail screens to the repository; add reconcile action.
- [ ] Add string resources for all codes and states.
- [ ] Add previews for every UI state.

## 8. Files

| Action | Path |
| --- | --- |
| Add | `ui/card/CardFlowViewModel.kt` |
| Add | `ui/card/CardUiState.kt` |
| Add | `ui/card/PresentCardScreen.kt` |
| Add | `ui/card/CardProcessingScreen.kt` |
| Add | `ui/card/CardResultScreen.kt` |
| Add | `ui/card/pin/PinSurface.kt` |
| Change | `ui/amount/AmountScreen.kt` |
| Change | `ui/navigation/route/MainGraphRoute.kt` |
| Change | `ui/navigation/navGraph/MainNavGraph.kt` |
| Change | `ui/transaction/TransactionHistoryScreen.kt` |
| Add | `ui/transaction/TransactionDetailScreen.kt` |
| Change | `res/values/strings.xml` |
| Change | `di/` (view model and flow bindings) |

## 9. Tests

- Compose tests for every state in the table using the fake terminal.
- Cancel at card wait, at PIN and at confirmation.
- Back during processing is ignored.
- Unavailable readiness disables purchase and explains why.
- Result screen for approved, declined and unknown.
- PIN surface: digits, backspace, cancel, blocked, timeout, TalkBack labels.

## 10. Acceptance criteria

- A cashier can complete an approved purchase on an emulator without touching
  the fake terminal code.
- No screen can leave a session running in the background.
- PIN values are never read, logged or retained by app code.
- Every UI state has a string resource and a preview.
- TalkBack can operate amount entry, PIN entry and the result screen.
