---
name: pos-agent-architecture
description: "Use when adding, moving, renaming, or reviewing files and classes in POS Agent: hexagonal ports and adapters, domain model vs port folders, use cases, repository/gateway implementations, and role suffixes (UseCase, Impl, Mapper, Dto)."
license: MIT
---

# POS Agent — Architecture & Naming

This project is a Kotlin/Android card-acceptance app organised as one `:app` module with layered packages. The rules below keep the dependency direction and file naming consistent. `ARCHITECTURE.md` is the source of truth for behaviour; this skill is the source of truth for structure and names.

## When to use

Use this skill when:

- adding a new feature, use case, repository, gateway, or mapper
- deciding whether a file belongs in `model/`, `port/`, `usecase/`, or `data/`
- naming a new class or file
- reviewing a diff for structure or naming drift

---

## 1. Layers and dependency rule

```
ui/       presentation   -> depends on domain
data/     adapters       -> depends on domain
domain/   business rules -> depends on nothing
core/     shared kernel  -> used by all layers
```

- `domain/` must not import `android.*`, `androidx.*`, Retrofit, Room, or Compose.
- `data/` implements the ports declared in `domain/`.
- `ui/` calls use cases only; it never sees DTOs or database entities.

Dependencies point inward:

```
ui  ──┐
      ├──> domain
data ─┘
```

---

## 2. Folder layout

Organise by bounded context, then by role inside the context.

```
domain/
├── card/
│   ├── model/          # entities + value objects + request/result data classes
│   ├── port/           # interfaces ONLY (no data classes)
│   ├── event/
│   └── usecase/        # use case classes
├── terminal/
│   ├── model/
│   └── port/
├── settlement/
│   ├── model/
│   └── port/
├── merchant/
└── shared/             # cross-context value objects (Money)

data/
├── card/
│   ├── repository/     # <Port>Impl classes
│   ├── remote/nibss/   # gateway impl + dto/ + mapper/
│   └── mapper/
├── terminal/
└── settlement/

core/
├── error/
├── result/
├── time/
└── ui/
```

`data/` mirrors `domain/` one context at a time. Never keep flat top-level `data/repository` when a context folder exists.

### Hard rules

1. **`port/` contains interfaces only.** No `data class`, no `sealed class`, no implementations.
2. **`model/` contains every data class and value object** used by the domain, including gateway request/result types. Do not leave a data class inside a port file.
3. **Use cases live in `usecase/`** and are the only entry points the UI should call.
4. **Adapters live in `data/<context>/`** and implement the domain ports.
5. One public top-level type per file; **filename == type name** (small related value objects may be grouped, e.g. `References.kt`).

---

## 3. Naming convention

Append the role so the file type is obvious from the name.

| Kind | Suffix | Example |
| --- | --- | --- |
| Use case | `UseCase` | `ProcessPurchaseUseCase` |
| Port / interface | role name, **no** `Impl` | `CardTransactionRepository`, `NibssCardGateway` |
| Repository implementation (`data/`) | `Impl` | `CardTransactionRepositoryImpl` |
| Gateway implementation (`data/`) | `Impl` | `NibssCardGatewayImpl` |
| Mapper | `Mapper` | `CardTransactionMapper` |
| DTO | `Dto` | `AuthorizationRequestDto` |
| Screen | `Screen` | `HomeScreen` |
| ViewModel | `ViewModel` | `HomeViewModel` |
| Command / request / result / event / model | already self-describing | `PurchaseCommand`, `CardAuthorizationRequest`, `CardOperationResult`, `CardTransactionEvent`, `CardTransaction` |

### Rules

- **`Impl` is reserved for implementations.** An interface must never end in `Impl`; the `...Impl` class is the thing in `data/` that implements it.
- **Models and value objects get no role suffix.** `CardTransaction` → `CardTransactionModel` is noise; the `model/` folder already says what it is.
- **Ports keep their role name.** `Clock`, `NibssCardGateway`, `CardTransactionRepository` are already complete names.
- **File name matches the class name.**

### Examples

| Before | After |
| --- | --- |
| `ProcessPurchase` / `ProcessPurchase.kt` | `ProcessPurchaseUseCase` / `ProcessPurchaseUseCase.kt` |
| `RefundTransaction` / `RefundTransaction.kt` | `RefundTransactionUseCase` / `RefundTransactionUseCase.kt` |
| `ReverseTransaction` / `ReverseTransaction.kt` | `ReverseTransactionUseCase` / `ReverseTransactionUseCase.kt` |
| `NibssCardGateway` implementation | `NibssCardGatewayImpl` in `data/card/remote/nibss/` |
| `CardTransactionRepository` implementation | `CardTransactionRepositoryImpl` in `data/card/repository/` |

---

## 4. Port files stay thin

A port file contains the interface and, at most, KDoc. Move every data class to `model/`:

```kotlin
// domain/card/port/NibssCardGateway.kt
package com.example.pos_agent.domain.card.port

import com.example.pos_agent.core.result.Outcome
import com.example.pos_agent.domain.card.model.CardAuthorizationRequest
import com.example.pos_agent.domain.card.model.CardOperationResult
import com.example.pos_agent.domain.card.model.CardRefundRequest
import com.example.pos_agent.domain.card.model.CardReversalRequest
import com.example.pos_agent.domain.card.model.TransactionReference
import com.example.pos_agent.domain.card.model.TransactionStatus

interface NibssCardGateway {
    suspend fun authorize(request: CardAuthorizationRequest): Outcome<CardOperationResult>
    suspend fun reverse(request: CardReversalRequest): Outcome<CardOperationResult>
    suspend fun refund(request: CardRefundRequest): Outcome<CardOperationResult>
    suspend fun query(reference: TransactionReference): Outcome<CardOperationResult>
}
```

```kotlin
// domain/card/model/CardAuthorizationRequest.kt
package com.example.pos_agent.domain.card.model

data class CardAuthorizationRequest(
    val reference: TransactionReference,
    val stan: Stan,
    val terminalId: String,
    val merchantId: String,
    val amount: Money,
    val card: CardToken,
    val maskedPan: MaskedPan,
)
```

---

## 5. Checklist for a new feature

1. Pick the bounded context (`card`, `terminal`, `settlement`, `merchant`).
2. Add entities and value objects under `domain/<context>/model/`.
3. Declare the outbound interface under `domain/<context>/port/` (no data classes).
4. Put request/result/command data classes in `model/`, not in the port or use case file.
5. Add the use case under `domain/<context>/usecase/` with a `UseCase` suffix.
6. Implement the port under `data/<context>/` with an `Impl` suffix.
7. Keep DTOs and mappers inside the adapter (`data/<context>/remote/.../dto/`, `.../mapper/`).
8. Update `ARCHITECTURE.md` when the structural rules or the use case table change.
