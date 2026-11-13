# Testing

How the project is tested: the strategy per layer, the conventions every test follows, where test
code lives, and how to run the suites locally and in CI. Code templates for each layer are in
[WRITING-TESTS.md](WRITING-TESTS.md).

## Contents

- [Test framework and setup](#test-framework-and-setup)
- [Strategy](#strategy)
  - [Layer to approach](#layer-to-approach)
  - [Mockk policy](#mockk-policy)
- [Conventions](#conventions)
  - [One test file per class](#one-test-file-per-class)
  - [Test names](#test-names)
  - [Test body and assertions](#test-body-and-assertions)
- [Where test code lives](#where-test-code-lives)
  - [The core:testing module](#the-coretesting-module)
  - [Test doubles](#test-doubles)
  - [Test data factories](#test-data-factories)
  - [Feature-local test helpers](#feature-local-test-helpers)
  - [Test tags for Compose testing](#test-tags-for-compose-testing)
- [Running tests](#running-tests)
- [Instrumented Compose UI tests](#instrumented-compose-ui-tests)
- [Architecture tests](#architecture-tests)
- [Coverage](#coverage)
- [CI integration](#ci-integration)

## Test framework and setup

The project uses **JUnit 4** with a set of supporting libraries for fakes, assertions, flows and
Android-on-JVM testing.

| Library | Version | Purpose |
|---------|---------|---------|
| JUnit 4 | 4.13.2 | Test runner and assertions |
| Mockk | 1.14.6 | Kotlin mocking (restricted, see [Mockk policy](#mockk-policy)) |
| Truth | 1.4.5 | Fluent assertions (`assertThat(...)`) |
| Turbine | 1.2.1 | Testing `Flow` emissions |
| Robolectric | 4.16 | Android framework on the JVM |
| kotlinx-coroutines-test | 1.9.0 | Test dispatchers and `runTest` |
| Compose UI Test JUnit4 | 1.9.0 | Compose UI component testing |
| Konsist | 0.17.3 | Architecture rule tests |
| mockwebserver | 4.12.0 | HTTP interception for remote datasource tests |

Hilt and WorkManager testing support is also included:

| Library | Purpose |
|---------|---------|
| `hilt-android-testing` | Hilt dependency injection in instrumented tests |
| `work-testing` | WorkManager test drivers for sync worker tests |

All test dependencies are managed in the version catalog (`gradle/libs.versions.toml`) and
applied by the convention plugins in `build-logic/convention/`. No extra setup is needed beyond
cloning the repository and running `./gradlew build`. Instrumented tests need a connected device
or emulator running Android 8 (SDK 26) or higher.

## Strategy

The layer a class belongs to decides how it is tested: which collaborators are real, which are
fakes, and which dispatcher drives coroutines. Persistence and network edges are tested against
real engines (Room, FhirEngine, MockWebServer); everything above them is tested with hand-written
fakes from `core:testing`.

### Layer to approach

| Layer | Approach |
|---|---|
| Room DAO (`core:database`) | Real `Room.inMemoryDatabaseBuilder` + Robolectric; synchronous, no `runTest`; `database.close()` in `@After` |
| Room `XxxStoreImpl` (`core:database`) | `TestXxxDao` fakes + `TestDispatcherProvider(UnconfinedTestDispatcher())`, or real in-memory Room + Robolectric when the store takes `EHACareDatabase` |
| `RemoteDataSourceImpl` (`core:data`) | `MockWebServer` + real Retrofit; `shutdown()` in `@After` |
| `RepositoryImpl` (`core:data`) | `TestXxxStore` / `TestXxxRemoteDataSource` fakes; `TestTransactionRunner` where a method opens a transaction |
| `*UnitOfWorkImpl` / `*Persister` (`core:data`) | `TestXxxStore` fakes + `TestTransactionRunner`: write order per params shape, a failing write stops the rest, practitioner stamping on creates. Rollback is not proven (the runner is pass-through); a real-FhirEngine rollback test is the target, blocked while the `core:fhir` store impls are `internal` |
| `UseCase` (`core:domain`, orchestrating only) | `TestXxxRepository` / `TestXxxUnitOfWork` fakes; assert the params handed to the write call; no `TestTransactionRunner` (use cases never open transactions) |
| Domain params builder (`org.eha.domain.mapper.<context>`) | Plain JVM test, no fakes |
| `ViewModel` (`feature:*`) | `TestXxxRepository` / `TestXxxUnitOfWork` fakes injected directly, plus the **real** orchestrating use case if the ViewModel uses one; `MainDispatcherRule(StandardTestDispatcher())` + `advanceUntilIdle()` |
| Mapper / Validator / Util | Plain JVM test; Mockk acceptable |

ViewModels inject repositories directly (there are no pass-through use cases), so they are built
over `TestXxxRepository` fakes. Where a ViewModel uses an orchestrating use case, build the real
use case over the fakes, never a mock. FHIR mapper tests are covered in
the "Mapper tests" section of the FHIR Mappers guide (`FHIR-MAPPERS.md` in the fhir-mapper skill, `docs/guides/FHIR-MAPPERS.md`).

### Mockk policy

**Never Mockk:** Room DAOs, Retrofit API service interfaces, store and datasource interfaces,
repository and unit-of-work interfaces, and use cases. Write or reuse a `TestXxx` fake instead.

**Mockk acceptable:** mappers, validators, utils, `Context` / `WorkerParameters`, loggers,
`FirebaseRemoteConfig`, and Gradle `Project` in `build-logic`.

**FhirEngine:** prefer a real in-memory engine under Robolectric. Mockk is tolerated in older tests
still being migrated, or when a test checks delegation and wiring rather than persistence.

Prefer a shared fake over a relaxed mock wherever one exists; for example
`TestEHACareFhirSyncManagerImpl` (`org.eha.testing.fake.sync`) is the fake for `FhirSyncManager`
and `TestSyncOperationsProvider` the fake for `SyncOperationsProvider`.

## Conventions

### One test file per class

**One `{ClassName}Test.kt` per class.** No `UnitTest` or `IntegrationTest` suffix: the test type
and its tooling follow from the layer, not the file name.

| Location | Naming pattern | Example |
|----------|---------------|---------|
| `src/test/` | `{ClassName}Test.kt` | `PatientListViewModelTest.kt`, `PatientRepositoryImplTest.kt` |
| `src/test/kotlin/` (`:konsist-test`) | `*Test.kt` per concern | `FeatureModuleTest.kt`, `CoreDomainTest.kt` |
| `src/androidTest/` | `*Test.kt`, `*SemanticsTest.kt` for accessibility semantics | `BmiReadOnlyFieldSemanticsTest.kt` |

Test packages mirror the main packages (for features: `org.eha.<feature>.screen.<screenName>`,
with no `presentation` segment).

### Test names

Use backtick names in the form `` `<action> should <expected outcome> when <condition>` ``:

```kotlin
@Test
fun `upsertPatient should emit error when consents are not confirmed in create mode`() =
    runTest { ... }
```

The whole declaration line, `    fun \`name\`() = runTest {` included, must stay **under 100
characters**. Shorten the name or break after `=`; this project limit is stricter than Detekt's
120-character `MaxLineLength`, and applies to every line of a test file.

### Test body and assertions

- **Arrange / Act / Assert** with explicit `// Arrange`, `// Act`, `// Assert` comments.
- **Truth** for assertions (`assertThat(...)`); `kotlin.test` is not used.
- **Turbine** for `Flow`s (`.test { }`, `awaitItem()`, `cancelAndIgnoreRemainingEvents()`).
- **Exceptions:** `@Test(expected = ExceptionClass::class)` with the throwing call under
  `// Act & Assert`; never try/catch with a boolean or `assertFailsWith`.
- **ViewModels:** `MainDispatcherRule(StandardTestDispatcher())` and `advanceUntilIdle()` after
  every async action. Other layers use the default `MainDispatcherRule()` (Unconfined).
- `@get:Rule` (not `@Rule`) and `@OptIn(ExperimentalCoroutinesApi::class)` on the class.
- `// ========== SECTION ==========` banners, one section per method or action, with helper
  methods last. Within a section: happy path, then variations, then errors.

The section order for each kind of test file and a full checklist are in
[WRITING-TESTS.md](WRITING-TESTS.md#section-ordering).

## Where test code lives

| Kind | Location |
|---|---|
| Tests | `src/test/java/{package}/` next to the code under test: `core:data` for repositories, units of work, persisters, datasources and mappers; `core:domain` for use cases and validators; the feature for ViewModels and UI mappers |
| **Every** test double, `MainDispatcherRule`, the shared Robolectric `Application` | `core:testing` `src/main/` (`org.eha.testing.{fake,rule,application}`) |
| Test-data factories for model, FHIR, DTO, entity and `core:ui` types | `core:testing` `src/main/` (`org.eha.testing.factory.*`) |
| Factories for presentation types owned by one feature (`*Ui`, entry state) | that feature's `src/test/java/{package}/factory/` |

For a new feature this means, for example:

```
feature/{name}/src/test/java/org/eha/{name}/
├── screen/{screenName}/{ScreenName}ViewModelTest.kt
├── mapper/{Name}UiMapperTest.kt
└── factory/{Name}UiTestFactory.kt

core/domain/src/test/java/org/eha/domain/usecase/{context}/{Name}UseCaseTest.kt
core/data/src/test/java/org/eha/data/repository/{context}/{Name}RepositoryImplTest.kt
core/data/src/test/java/org/eha/data/unitofwork/{area}/{Thing}UnitOfWorkImplTest.kt   (cross-aggregate saves only)
core/data/src/test/java/org/eha/data/mapper/{context}/{Name}MapperTest.kt
core/testing/src/main/java/org/eha/testing/fake/domain/repository/{context}/Test{Name}Repository.kt
core/testing/src/main/java/org/eha/testing/fake/domain/unitofwork/{area}/Test{Thing}UnitOfWork.kt
core/testing/src/main/java/org/eha/testing/factory/model/{context}/{Name}ModelTestFactory.kt
```

### The core:testing module

`core:testing` is the single home for every test double, test-data factory, the shared
Robolectric `Application` and the shared JUnit rule. There are no `testFixtures` source sets and
no per-module `testUtils/` doubles. Modules consume it as a plain test dependency (feature modules
get it from the convention plugin):

```kotlin
dependencies {
    testImplementation(project(":core:testing"))
}
```

```
org.eha.testing
├── application/   EHACareTestApplication: the only test Application, via @Config(application = ...)
├── rule/          MainDispatcherRule
├── fake/<layer>/  TestXxx doubles, mirroring the package of the faked interface
│   ├── domain/{repository/<context>,unitofwork/<area>,manager,service}
│   ├── data/{datasource,store}
│   ├── fhir/store
│   ├── database/{dao,store}
│   └── common/ security/ auth/ cache/ config/ sync/
└── factory/       one <ClassName>TestFactory.kt per class
    ├── model/<context>/     domain models and params
    ├── fhir/  fhir/request/ FHIR resources (Fhir-prefixed) and store requests
    ├── dto/  entity/        network DTOs, Room entities
    └── ui/                  core:ui types (LocationOptionUi, TextInputFieldState, ReferralSummaryUi)
```

- **`MainDispatcherRule`** (`org.eha.testing.rule`) sets `Dispatchers.Main` to an
  `UnconfinedTestDispatcher` (or the dispatcher you pass) and resets it after each test.
- **`EHACareTestApplication`** (`org.eha.testing.application`) initialises FhirEngine in test mode;
  every Robolectric test uses it and no module declares its own test `Application`.

`core:testing` depends on the core modules whose interfaces it fakes, so it must stay a test-only
dependency: nothing in `main` may depend on it. See `core/testing/README.md` for the full catalog.

### Test doubles

- Every double uses the `Test` prefix (`TestPatientRepository`, `TestEncounterStore`,
  `TestAuthRemoteDataSource`) and lives in `core:testing` under `org.eha.testing.fake.<layer>`,
  in the package mirroring the faked interface: `fake.domain.repository.<context>`,
  `fake.domain.unitofwork.<area>`, `fake.domain.manager`, `fake.domain.service`,
  `fake.data.datasource`, `fake.data.store`, `fake.fhir.store`, `fake.fhir.transaction`, `fake.database.{dao,store}`,
  `fake.common`, `fake.security`, `fake.auth`, `fake.cache`, `fake.config`, `fake.sync`.
- When you add a repository, unit-of-work, store, datasource or manager interface, add its fake
  there.
- `TestXxxUnitOfWork` records `lastParams` / `commitCount` and returns a configurable `result`.
- `TestTransactionRunner` (`fake.fhir.transaction`) is pass-through with no real rollback; it is used
  by `RepositoryImpl` and `*UnitOfWorkImpl` tests, never by use case tests.

The shape of a double (error toggles, spies, `reset()`) and the full list of existing doubles are
in [WRITING-TESTS.md](WRITING-TESTS.md#test-doubles).

### Test data factories

- One `<ClassName>TestFactory.kt` per class (FHIR resources prefixed `Fhir`, e.g.
  `FhirPatientTestFactory.kt`) exposing a top-level `test<ClassName>(...)` with a fixed
  deterministic default for every parameter: `testPatientModel()`, `testFhirPatient()`,
  `testCreatePatientRequest()`, `testLabTestOptionDto()`, `testLabTestOptionEntity()`.
- Variant presets are `test<Scenario><ClassName>()` (`testDiagnosisGuidedEncounterAiResult()`);
  add one only when 3+ tests share it.
- A test never asserts on a value it did not pass: pass every asserted value explicitly, since
  defaults can change.
- No local model-builder helpers in tests. Seed/scenario helpers that go through a fake's public
  API, and `createViewModel()` SUT builders, are fine.
- `TestXxx` classes are doubles; `testXxx()` functions are data.

### Feature-local test helpers

A feature's `src/test/` holds only its ViewModel and UI-mapper tests plus factories for its own
presentation types:

| Module | Feature-local test helpers |
|--------|---------|
| `feature:encounter` | `factory/`: `DiagnosisOptionUiTestFactory`, `LabTestUiTestFactory`, ... |
| other features | none; use `core:testing` doubles and factories |

### Test tags for Compose testing

Compose test tags are defined in `testing/` packages in `main` (not in test source sets), so unit
and instrumented tests can share them:

| Module | Test tags file |
|--------|---------------|
| `app` | `org.eha.ehacare.testing.AppTestTags` |
| `feature:home` | `org.eha.home.testing.HomeTestTags` |
| `feature:patient` | `org.eha.patient.testing.PatientTestTags` |
| `feature:encounter` | `org.eha.encounter.testing.EncounterTestTags` |
| `feature:auth` | `org.eha.auth.testing.AuthTestTags` |

## Running tests

```bash
# All unit tests, all modules and variants
./gradlew test

# Full build including tests
./gradlew build

# Debug variants (tests are configured for Debug only)
./gradlew testDevDebugUnitTest
./gradlew testProdDebugUnitTest

# One Android module
./gradlew :feature:patient:testDevDebugUnitTest
./gradlew :core:data:testDevDebugUnitTest
./gradlew :core:domain:testDevDebugUnitTest

# The pure-JVM core:model module has no variants
./gradlew :core:model:test

# Konsist architecture rules (pure-JVM :konsist-test module, always re-runs)
./gradlew :konsist-test:test

# Instrumented tests (connected device or emulator)
./gradlew connectedAndroidTest
./gradlew connectedDevDebugAndroidTest
./gradlew :feature:encounter:connectedDevDebugAndroidTest
./gradlew :core:ui:connectedDevDebugAndroidTest
```

### Maestro E2E tests

End-to-end UI tests are YAML flows in `.maestro/`. The preferred runner is
`scripts/run-maestro-smoke.sh`, which runs the smoke suites against deterministic local WireMock
doubles by default (`--backend mock`) or against the real dev services (`--backend dev`):

```bash
./scripts/run-maestro-smoke.sh                    # fast-lane suite, mock backend
./scripts/run-maestro-smoke.sh --tier nightly     # full suite + protocol scenarios, mock
./scripts/run-maestro-smoke.sh --backend dev      # real dev services (needs .maestro/env.json)
```

See `docs/testing/E2E-MAESTRO.md` for harness startup, tiers, scenarios, fixture editing and
failure diagnosis, and `.maestro/README.md` for the environment-file table. Individual flows can also be run with the Maestro CLI:

```bash
maestro test .maestro/login_test.yaml
maestro test .maestro/add_patient_test.yaml
```

These flows read `${LOGIN_USERNAME}` and `${LOGIN_PASSWORD}` from Maestro's environment.

## Instrumented Compose UI tests

Compose UI tests live in `src/androidTest/`, use `createComposeRule()` and run with the
`connectedAndroidTest` tasks above. Wrap content in `EHACareTheme` so the design system is
provided. Condensed from `core/ui/src/androidTest/.../EHACareInputErrorSemanticsTest.kt`:

```kotlin
class EHACareInputErrorSemanticsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun liveRegionMode_isPolite_onErrorTextNode() {
        val errorText = "This field is required"
        composeTestRule.setContent {
            EHACareTheme {
                EHACareInputError(text = errorText)
            }
        }

        composeTestRule
            .onNodeWithText(errorText)
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.LiveRegion,
                    LiveRegionMode.Polite,
                ),
            )
    }
}
```

Find nodes with `onNodeWithText(...)`, `onNodeWithContentDescription(...)` or
`onNodeWithTag(...)` (tags from the `*TestTags` objects), and interact with `performClick()`,
`performTextInput(...)`, `assertIsDisplayed()`, `assertIsEnabled()`.

## Architecture tests

Module boundaries and naming rules are enforced by Konsist tests in the root-level pure-JVM
`:konsist-test` module (`konsist-test/src/test/kotlin/org/eha/konsist/`, one file per concern).
The module has no project dependencies (Konsist parses sources itself) and its test task always
re-runs, so a violation anywhere is reported. Run it with `./gradlew :konsist-test:test`; CI runs
it on every pull request. Among other things it fails the build when a `Test*` double is declared
outside `core:testing` or a factory function is not named `test*`.

The rules, and how to fix a failure, are described in
`ARCHITECTURE-RULES.md` in the eha-module-architecture skill (`docs/architecture/ARCHITECTURE-RULES.md`).

## Coverage

No coverage thresholds are configured; the project does not use JaCoCo, Kover or any coverage
gate in CI.

## CI integration

`.github/workflows/build_test-dev.yaml` runs on pushes to `develop` and `sprint/**` and on pull
requests (except to `master` / `release/**`). Its jobs run Spotless and Android lint, the unit
tests (`./gradlew testDevDebugUnitTest`), the Konsist architecture rules
(`./gradlew :konsist-test:test`) and the dev debug build.

A separate workflow, `.github/workflows/ai-code-review.yml`, posts an automated AI code review on
`pull_request` events. It does not run Gradle.
