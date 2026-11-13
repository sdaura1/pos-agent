# Architecture

POS Agent is a **card-acceptance app**, not a full retail POS. The domain is
centred on a single `CardTransaction` aggregate and the operations around it:
purchase, reversal and refund.

The project uses a layered, domain-driven arrangement inside one `:app`
module. The layers are packages today; the stable ones can be promoted to
Gradle modules later without changing the code.

```
ui/       presentation   -> depends on domain
data/     adapters       -> depends on domain
domain/   business rules -> depends on nothing
core/     shared kernel  -> used by all layers
```

## The dependency rule

Dependencies point inward:

```
ui  ──┐
      ├──> domain
data ─┘
```

- `domain/` must not import `android.*`, `androidx.*`, Retrofit, Room or any
  Compose type. It is plain Kotlin and unit-testable on the JVM.
- `data/` implements the ports declared in `domain/`.
- `ui/` calls use cases and renders state. ViewModels never see DTOs or
  database entities.

## Bounded contexts

| Context | Responsibility |
| --- | --- |
| `auth` | Operator sign-in and durable session state |
| `card` | Card purchase, reversal, refund, reconciliation |
| `terminal` | Terminal and merchant credentials (TID/MID) |
| `settlement` | End-of-day batch settlement and reconciliation reports |
| `merchant` | Merchant configuration (reserved) |

Cross-context references use identifiers (terminal ID, merchant ID) rather
than object references.

## The card transaction aggregate

`domain/card/model/CardTransaction.kt` is the aggregate root:

- **Type**: `PURCHASE`, `REVERSAL`, `REFUND`.
- **Status**: `INITIATED`, `APPROVED`, `DECLINED`, `UNKNOWN`, `REVERSED`,
  `REFUNDED`.
- A refund or reversal is its **own transaction** linked to the original via
  `originalTransactionId`. Records are never merged or mutated away.
- A purchase may be refunded partially; approved refunds can never exceed the
  original amount.
- `UNKNOWN` is first-class: a NIBSS timeout does not prove the transaction
  failed, so it must be reconciled rather than retried blindly.

`TransactionReference` is the idempotency key. Retries reuse it.
`Stan` (6 digits) and `Rrn` (12 digits) are value objects.

## Card data

The domain never sees a full PAN. `CardToken` is opaque, already-encrypted
data from the card reader; `MaskedPan` refuses to hold an unmasked 13–19 digit
number. Only masked values are persisted or displayed.

## NIBSS boundary

NIBSS is an external system, not a bounded context. It is reached through the
`NibssCardGateway` port in `domain/card/port/`. The adapter in
`data/remote/nibss/` owns:

- transport (ISO 8583 or REST) and authentication/signing
- request/response DTOs
- mapping NIBSS response codes to `TransactionStatus`

The domain only sees `CardAuthorizationRequest`, `CardReversalRequest`,
`CardRefundRequest` and `CardOperationResult`.

## Authentication

Sign-in is a separate bounded context reached through `AuthRepository` in
`domain/auth/port/`. `AuthRepositoryImpl` calls the remote auth service with
Retrofit and stores the resulting `AuthSession` through the `SessionStore`
port. Access and refresh tokens are encrypted with an Android Keystore AES/GCM
key before they reach DataStore, and are excluded from backup. Passwords are
never stored.

`MainViewModel` is the activity-scoped root state holder. It restores the
stored session at startup and exposes `MainUiState` (wrapping `MainLoadState`)
plus the current `AuthSession`. `MainLoadState` is `Loading` or
`Loaded(AppAuthState)`; `AppAuthState` is `AUTHENTICATED` or `UNAUTHENTICATED`.
`LoginViewModel` remains form-specific.

The UI has a single auth gate: `AppRoot` composes the root `NavHost` and starts
at the auth graph when unauthenticated or the signed-in graph when
authenticated. Because login is not part of the signed-in graph, Back cannot
reveal a protected screen.

The auth service base URL is supplied at build time via the `AUTH_BASE_URL`
Gradle property. Invalid credentials must return `401`; the client maps that
to a generic "Invalid username or password" message.

Auth failures carry a stable `DomainError.code`. The UI resolves that code to
a string resource, so the adapters never contain user-facing text.

## Navigation

Routes are serializable types (`AppRoute`, `AuthGraphRoute`, `MainGraphRoute`)
rather than strings. `authNavGraph` owns Login, Reset PIN and Set PIN. The
signed-in graph contains Dashboard, Transaction History, Amount and Profile.
The dashboard is the navigation hub; there is no persistent bottom navigation.

## Dependency injection

Hilt wires the app. `PosAgentApplication` is annotated `@HiltAndroidApp`,
`MainActivity` uses `@AndroidEntryPoint`, and Compose obtains ViewModels with
`hiltViewModel()`. Network providers live in `di/NetworkModule`, port bindings
in `di/AuthModule` and `di/StorageModule`, and shared providers in
`di/AppModule`. Use cases and adapters are constructor-injected.

## Use cases

| Use case | Rule |
| --- | --- |
| `LoginUseCase` | Validate input and authenticate over TLS |
| `RestoreSessionUseCase` | Restore a stored, unexpired session |
| `LogoutUseCase` | Clear the stored session |
| `ProcessPurchaseUseCase` | Persist locally, authorise, then apply APPROVED/DECLINED/UNKNOWN |
| `ReverseTransactionUseCase` | Only an approved purchase with an RRN can be reversed |
| `RefundTransactionUseCase` | Only an approved purchase; partial refunds capped at the original amount |

Use cases orchestrate aggregates and ports. They contain no Android or Compose
code.

## Responses and errors

Every adapter returns `core/result/Response<T>` — `Success(value)` or
`Failure(DomainError)` — regardless of whether the value came from NIBSS (REST
or ISO 8583), the local database, or a socket. Transport exceptions are
translated into `DomainError` inside `data/`; nothing transport-specific
escapes the adapter.

## Money

`Money` stores `Long` minor units (kobo) plus a `Currency`. Never use
`Double`/`Float` for money. Card amounts cross the NIBSS boundary in minor
units without conversion.

## Growth path

1. **Now:** one module, packages as above.
2. **Enforce:** extract `domain/` into a `:core:domain` pure Kotlin JVM module
   so the compiler rejects Android imports.
3. **Scale:** add `:core:data`, `:core:ui`, and `:feature:*` modules that each
   depend only on what they need.
