# POS Agent — Documentation

Planning and design documents. Code lives under `app/src/main/java/com/example/pos_agent/`.

| Document | Purpose |
| --- | --- |
| [`../ARCHITECTURE.md`](../ARCHITECTURE.md) | Source of truth for layers, bounded contexts and the dependency rule |
| [`card-flow/01-overview.md`](card-flow/01-overview.md) | Goal, scope, architecture and slice plan for the card transaction flow |
| [`card-flow/02-domain-model.md`](card-flow/02-domain-model.md) | Card capture model, request changes, error taxonomy |
| [`card-flow/03-emv-session.md`](card-flow/03-emv-session.md) | Vendor-neutral EMV interaction state machine |
| [`card-flow/04-ports-and-fakes.md`](card-flow/04-ports-and-fakes.md) | Ports, fake terminal and scenario matrix |
| [`card-flow/05-persistence.md`](card-flow/05-persistence.md) | Storage, outbox and reconciliation |
| [`card-flow/06-ui-flow.md`](card-flow/06-ui-flow.md) | Screens, routes, view model and PIN entry |
| [`card-flow/07-receipts.md`](card-flow/07-receipts.md) | Receipt document and rendering rules |
| [`card-flow/08-nibss-adapter.md`](card-flow/08-nibss-adapter.md) | NIBSS gateway adapter, mapping and retry policy |
| [`card-flow/09-kozen-adapter.md`](card-flow/09-kozen-adapter.md) | Kozen SDK adapter and device bring-up (blocked) |
| [`card-flow/10-testing-strategy.md`](card-flow/10-testing-strategy.md) | Test layers, scenario matrix and quality gates |
| [`card-flow/11-risks-and-decisions.md`](card-flow/11-risks-and-decisions.md) | Risks, open decisions and assumptions |
| [`card-flow/12-nibss-ctms-spec.md`](card-flow/12-nibss-ctms-spec.md) | Digest of the NIBSS CTMS POS interface specification v1.0.2 |
| [`card-flow/13-key-exchange.md`](card-flow/13-key-exchange.md) | Key exchange, provisioning state machine and information checklist |
| [`card-flow/14-key-exchange-implementation.md`](card-flow/14-key-exchange-implementation.md) | Detailed implementation notes for the key-exchange slice |

## Conventions

- Tasks use checkboxes so progress can be tracked in place.
- "Blocked" means it depends on external input: hardware, firmware, credentials or certification.
- Vendor SDK types (`com.kozen.*`) never appear outside `data/device/kozen`.
- Domain code is plain Kotlin: no `android.*`, no Retrofit, no Compose.
- Estimates are developer-days for one developer familiar with the codebase.

## Current status

| Slice | Status | Depends on |
| --- | --- | --- |
| S1 — Domain, EMV session, ports, fakes | In progress: S1.1 done | Nothing |
| S2 — Persistence and reconciliation | Planned | S1 ports |
| S3 — UI flow and receipts | Planned | S1, S2 |
| S4 — NIBSS adapter | Partially blocked | Credentials, hash detail, certification scripts |
| S4a — Key exchange | In progress: S4a-1, S4a-2, S4a-3 done | NIBSS trace, key ownership, test host |
| S5 — Kozen adapter | Blocked | Hardware, firmware, certification, keys |
| S6 — Hardening | Planned | S3, S4 |

See [`card-flow/11-risks-and-decisions.md`](card-flow/11-risks-and-decisions.md) for the decisions that need to be settled before S2.
