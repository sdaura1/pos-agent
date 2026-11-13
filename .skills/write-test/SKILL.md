---
name: write-test
description: Write JVM tests for Android following EHACare project conventions. Layer determines test type — Room DAO tests use real Room + Robolectric in core:database, Room-backed StoreImpl uses TestXxxDao fakes (core:testing), uses real FhirEngine + Robolectric, RemoteDataSourceImpl uses MockWebServer, RepositoryImpl (core:data) uses TestXxxStore fakes (+ TestTransactionRunner where a method opens a transaction), cross-aggregate UnitOfWorkImpl and shared *Persister (core:data) use fake stores + TestTransactionRunner (rollback not provable yet), UseCase uses TestXxxRepository / TestXxxUnitOfWork fakes, ViewModel uses TestXxxRepository / TestXxxUnitOfWork fakes directly (plus the real orchestrating use case if it has one), domain params builders get plain JVM tests. One *Test.kt file per class (no UnitTest/IntegrationTest split). Mockk only for pure Kotlin classes (mappers, validators). Use when asked to write tests, add test coverage, create test doubles, or generate tests for ViewModels, UseCases, Repositories, UnitOfWorks, Persisters, DataSources, DAOs, or any Kotlin class in the EHACare Android project.
---

# Write Test Skill

This skill is the quick map plus the workflow; the full conventions are in `references/`.

- [references/TESTING.md](references/TESTING.md): strategy, layer → approach, Mockk policy,
  naming, where doubles and factories live, running tests
- [references/WRITING-TESTS.md](references/WRITING-TESTS.md): per-layer templates (DAO, FHIR
  store, Room store, remote datasource, repository, unit of work / persister, use case, ViewModel),
  Turbine idioms, test double shapes, the existing-doubles list, factories and the checklist
- Mapper test template: the "Mapper tests" section of `FHIR-MAPPERS.md` in the `fhir-mapper` skill

The same pages are mirrored for humans under `docs/`; update both copies when a page changes.

## Layer → Test Type Map

| Layer | Approach | Dependencies | Dispatcher / Notes |
|---|---|---|---|
| `Room DAO` (in `core:database`) | Real Room in-memory DB + Robolectric | `Room.inMemoryDatabaseBuilder`, `EHACareDatabase` | None — DAO methods are synchronous via `allowMainThreadQueries()`; no `runTest` |
| Room `XxxStoreImpl` (in `core:database`) | `TestXxxDao` fakes from `core:testing` (`org.eha.testing.fake.database.dao`) | `TestXxxDao`, `TestDispatcherProvider` | `TestDispatcherProvider(UnconfinedTestDispatcher())` injected directly — no Robolectric, no `MainDispatcherRule` |
| `RemoteDataSourceImpl` | MockWebServer + real Retrofit | `MockWebServer`, real API service | `UnconfinedTestDispatcher` via `MainDispatcherRule()` |
| `RepositoryImpl` | `TestXxxStore` (`org.eha.testing.fake.{fhir,database}.store`) + `TestXxxRemoteDataSource`; `TestTransactionRunner` when it opens transactions | No Mockk on store/datasource interfaces | `UnconfinedTestDispatcher` via `MainDispatcherRule()` |
| `*UnitOfWorkImpl` / `*Persister` (in `core:data`) | `TestXxxStore` fakes + `TestTransactionRunner`; build the real `*Persister` over the same fakes | Assert write order per params shape, that a failing write stops the rest, practitioner/facility stamping on creates. The pass-through runner cannot prove rollback (real-FhirEngine test blocked while `core:fhir` store impls are `internal`; keep the `// TODO`) | `UnconfinedTestDispatcher` via `MainDispatcherRule()` |
| `UseCase` | `TestXxxRepository` / `TestXxxUnitOfWork` (`org.eha.testing.fake.domain.unitofwork.<area>`) | No Mockk on repository or unit-of-work interfaces; assert the params passed to the write call (`lastParams`). Use cases never open transactions, so no `TestTransactionRunner` | `UnconfinedTestDispatcher` via `MainDispatcherRule()` |
| `ViewModel` | `TestXxxRepository` / `TestXxxUnitOfWork` fakes injected directly; real orchestrating use case only if the VM uses one | No Mockk on UseCases, repos or units of work | `StandardTestDispatcher()` via `MainDispatcherRule(StandardTestDispatcher())` + `advanceUntilIdle()` |
| Mapper / Validator / Util / domain params builder (`org.eha.domain.mapper.<context>`) | Mockk acceptable (builders: plain inputs, no fakes) | Pure Kotlin — no layer constraints | `UnconfinedTestDispatcher` via `MainDispatcherRule()` |

**Mockk is NEVER used on**: Room DAOs, Retrofit API service interfaces, store and datasource interfaces, repository and unit-of-work interfaces, or use case interfaces. Use the appropriate `TestXxx` fake instead.
**FhirEngine**: prefer real in-memory via Robolectric; Mockk is acceptable while tests are being migrated or when testing delegation/wiring rather than persistence semantics.

## Rules summary

- One `{ClassName}Test.kt` per class; no `UnitTest` / `IntegrationTest` suffix.
- Every `TestXxx` double lives in `core:testing` under `org.eha.testing.fake.<layer>`, mirroring
  the faked interface's package; add one there when a new interface needs it. Never in a module's
  `src/test`, never `testFixtures` (Konsist fails the build).
- Test data from `core:testing` factories `test<ClassName>(...)`; pass every value you assert on;
  no local model builders (seed helpers and `createViewModel()` are fine). Presentation-type
  factories go in the feature's `src/test/.../factory/`.
- Names `` `<action> should <expected outcome> when <condition>` ``; the full
  `    fun \`name\`() = runTest {` line stays under 100 chars.
- `// Arrange` / `// Act` / `// Assert` in every test; Truth assertions; Turbine for flows.
- ViewModels: `MainDispatcherRule(StandardTestDispatcher())` + `advanceUntilIdle()`.
- Exceptions: `@Test(expected = ...)` with `// Act & Assert — exception propagates out of runTest`;
  never try/catch or `assertFailsWith`.
- Section banners `// ========== X TESTS ==========`, helpers last; happy path → variations → errors.
- Module tasks: `./gradlew :module:testDevDebugUnitTest`; `:core:model:test` for the JVM model
  module; Konsist rules only in `:konsist-test` (never add Konsist tests to a feature or `app`).

## Workflow

1. Identify the layer of the class under test (table above).
2. Open the matching section of [references/WRITING-TESTS.md](references/WRITING-TESTS.md) and
   copy its template.
3. Check `core/testing/src/main/java/org/eha/testing/fake/` and `factory/` for existing doubles
   and factories before writing new ones; add missing ones to `core:testing`.
4. Write `{ClassName}Test.kt` with the right dispatcher, dependencies and section order.
5. Check every line is under 100 characters and walk the checklist at the end of
   [references/WRITING-TESTS.md](references/WRITING-TESTS.md#checklist).
6. Run the module's test task.
