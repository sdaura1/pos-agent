# S2 — Persistence, Outbox and Reconciliation

Status: planned
Slice: S2
Depends on: S1 ports and model
Effort: 4–6 developer-days
Related: [`02-domain-model.md`](02-domain-model.md), [`08-nibss-adapter.md`](08-nibss-adapter.md)

## 1. Goal

Implement `CardTransactionRepository` and the outbox so the local record is
crash-safe, `UNKNOWN` is first-class, and refunds/reversals are correctly
linked.

## 2. Decision

Room (recommended). DataStore remains for session only. Confirm before work
starts; see [`11-risks-and-decisions.md`](11-risks-and-decisions.md).

## 3. Schema

### card_transactions

| Column | Type | Notes |
| --- | --- | --- |
| id | TEXT PK | UUID from `TransactionId` |
| reference | TEXT UNIQUE | idempotency key |
| type | TEXT | enum name |
| status | TEXT | enum name |
| terminal_id | TEXT | |
| merchant_id | TEXT | |
| amount_minor | INTEGER | minor units, never floating point |
| currency | TEXT | ISO 4217, default NGN |
| stan | TEXT | |
| rrn | TEXT NULL | |
| auth_code | TEXT NULL | |
| response_code | TEXT NULL | |
| original_transaction_id | TEXT NULL | FK, indexed |
| masked_pan | TEXT NULL | masked only, enforced by domain |
| entry_mode | TEXT NULL | capture metadata |
| cvm | TEXT NULL | capture metadata |
| created_at | INTEGER | epoch millis |
| updated_at | INTEGER | epoch millis |

Indices: `reference` unique, `status`, `original_transaction_id`,
`created_at` descending for history.

### card_transaction_events (outbox)

| Column | Type | Notes |
| --- | --- | --- |
| id | TEXT PK | |
| transaction_id | TEXT FK | indexed |
| reference | TEXT | |
| type | TEXT | event class |
| payload | TEXT | serialised event |
| created_at | INTEGER | |
| dispatched_at | INTEGER NULL | null until published |

## 4. Behaviour

- Saving an aggregate and its events happens in **one** Room transaction.
- No raw PAN column exists. A test asserts the schema and rejects a capture
  containing an unmasked PAN before mapping.
- `findUnsettled()` returns `INITIATED` and `UNKNOWN` ordered by creation time.
- `findByOriginalId` powers refund/reversal ceilings.
- Amounts are stored in minor units; the mapper converts `Money` explicitly.
- Status transitions only go through the aggregate methods
  (`approve`, `decline`, `markUnknown`, `markReversed`, `markRefunded`).

## 5. Purchase orchestration change

`ProcessPurchaseUseCase` changes shape to match the interleaved kernel flow:

1. Acquire the card through `CardAcceptor`.
2. On `RequestOnline`, build the aggregate as `INITIATED` with the masked PAN
   and capture metadata, and persist it (plus the initial event if used).
3. Call `NibssCardGateway.authorize`.
4. Reply to the kernel with the host result.
5. On `Finished`, apply the final status and persist with an outbox event.
6. Return the final aggregate to the UI.

If the session ends before step 2, nothing is written. If the app dies between
steps 2 and 4, the row stays `INITIATED` and is reconciled.

## 6. Reconciliation

- `ReconcileTransactionUseCase`: for each unsettled transaction, query the host
  by reference, map the outcome, persist the final state and emit an outbox
  event.
- Runs on app start when authenticated, on demand from the history screen, and
  after connectivity returns.
- `UNKNOWN` is never retried as a new authorisation. Retrying is a decision for
  the host, not the client.
- A reconciliation that still cannot resolve keeps `UNKNOWN` and increases an
  attempt counter; after a threshold the record is flagged for manual review.

## 7. Tasks

- [ ] Add Room dependencies and verify KSP/AGP 9 compatibility with a trivial
      entity before writing the full schema.
- [ ] Add entities, converters, DAOs and the database class.
- [ ] Implement `CardTransactionRepositoryImpl` with atomic aggregate + outbox
      writes.
- [ ] Implement mappers between domain models and entities.
- [ ] Rewire `ProcessPurchaseUseCase` to the sequence in section 5.
- [ ] Add `ReconcileTransactionUseCase` and its scheduler point.
- [ ] Enforce refund/reversal ceilings against `findByOriginalId`.
- [ ] Add an event dispatcher stub that drains `dispatched_at IS NULL`.
- [ ] Add migration scaffolding even at version 1, so later migrations are
      possible.

## 8. Files

| Action | Path |
| --- | --- |
| Add | `data/local/db/PosAgentDatabase.kt` |
| Add | `data/local/db/entity/CardTransactionEntity.kt` |
| Add | `data/local/db/entity/OutboxEventEntity.kt` |
| Add | `data/local/db/dao/CardTransactionDao.kt` |
| Add | `data/local/db/dao/OutboxDao.kt` |
| Add | `data/local/mapper/CardTransactionMapper.kt` |
| Add | `data/repository/CardTransactionRepositoryImpl.kt` |
| Add | `domain/card/usecase/ReconcileTransactionUseCase.kt` |
| Change | `domain/card/usecase/ProcessPurchaseUseCase.kt` |
| Change | `di/StorageModule.kt` |

## 9. Tests

- In-memory database: save/search/unsettled/original-id queries.
- Atomicity: a failing outbox insert rolls back the aggregate insert.
- Crash injection: persist `INITIATED`, do not authorise, reopen the database,
  assert the row is returned by `findUnsettled()`.
- Refund ceiling: partial refunds cannot exceed the original approved amount.
- Mapping round-trip for every field, especially amounts and enums.

## 10. Acceptance criteria

- No code path stores an unmasked PAN.
- Aggregate + outbox writes are atomic.
- `INITIATED` and `UNKNOWN` survive process death and are reconcilable.
- Refund and reversal rules are enforced and tested at the repository boundary.
- Database versioning and migration scaffolding exist from the first release.
