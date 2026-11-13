# Writing Tests

Per-layer recipes and code templates for JVM tests. The strategy, the rules every test follows
(file naming, test names, Mockk policy, where doubles and factories live) and the commands to run
them are in [TESTING.md](TESTING.md); this page shows how to apply them to each layer.

## Contents

1. [Test anatomy](#test-anatomy)
   - [Naming and line length](#naming-and-line-length)
   - [Arrange, Act, Assert](#arrange-act-assert)
   - [Dispatcher and Robolectric per layer](#dispatcher-and-robolectric-per-layer)
   - [Exception tests](#exception-tests)
   - [Section ordering](#section-ordering)
2. [Persistence and network](#persistence-and-network)
   - [Room DAO (core:database)](#room-dao-coredatabase)
   - [Room StoreImpl (core:database)](#room-storeimpl-coredatabase)
   - [RemoteDataSourceImpl (MockWebServer)](#remotedatasourceimpl-mockwebserver)
3. [Data layer](#data-layer)
   - [RepositoryImpl (fake stores)](#repositoryimpl-fake-stores)
   - [UnitOfWorkImpl and Persister (fake stores)](#unitofworkimpl-and-persister-fake-stores)
4. [Domain and presentation](#domain-and-presentation)
   - [UseCase (fake repository or unit of work)](#usecase-fake-repository-or-unit-of-work)
   - [ViewModel (fake repositories)](#viewmodel-fake-repositories)
   - [Turbine flow testing](#turbine-flow-testing)
5. [Test doubles](#test-doubles)
   - [Double shape](#double-shape)
   - [Repository double](#repository-double)
   - [Unit-of-work double](#unit-of-work-double)
   - [Store double](#store-double)
   - [Existing doubles](#existing-doubles)
6. [Test data factories](#test-data-factories)
7. [Checklist](#checklist)

Mapper tests have their own template in the "Mapper tests" section of the FHIR Mappers guide (`FHIR-MAPPERS.md` in the fhir-mapper skill, `docs/guides/FHIR-MAPPERS.md`).

---

## Test anatomy

### Naming and line length

Pattern: `` `<action> should <expected outcome> when <condition>` `` (backtick names).

```kotlin
@Test
fun `init should load patients and group by first letter`() = runTest { ... }

@Test
fun `createPatient should return Success with patient ID`() = runTest { ... }

@Test
fun `upsertPatient should emit error when consents are not confirmed in create mode`() =
    runTest { ... }
```

Keep every line under 100 characters, including the full declaration
`    fun \`name\`() = runTest {`. Shorten the name, break at `= runTest`, or extract a variable:

```kotlin
// Too long
fun `offline fallback fails when credentials do not match stored hash and returns error`() = runTest {

// Break at = runTest
fun `offline fallback fails when credentials do not match stored hash`() =
    runTest {
        // ...
    }

// Too long
assertThat(viewModel.uiState.value.patients.filterIsInstance<PatientListItem.Patient>()).hasSize(3)

// Extract a variable
val patientItems = viewModel.uiState.value.patients
    .filterIsInstance<PatientListItem.Patient>()
assertThat(patientItems).hasSize(3)
```

### Arrange, Act, Assert

Every test marks its three parts with comments:

```kotlin
@Test
fun `createPatient should persist all fields to the datasource`() = runTest {
    // Arrange
    val params = testUpsertPatientParams(firstName = "Alice")

    // Act
    val result = useCase(params)

    // Assert
    assertThat(result).isInstanceOf(Resource.Success::class.java)
    val stored = testRepository.getAll().first()
    assertThat(stored.firstName).isEqualTo("Alice")
}
```

### Dispatcher and Robolectric per layer

| Layer | Dispatcher | Robolectric |
|---|---|---|
| Room DAO (`core:database`) | None: DAOs are synchronous via `allowMainThreadQueries()`; no `runTest` | Required |
| Room `XxxStoreImpl` (`core:database`) | `TestDispatcherProvider(UnconfinedTestDispatcher())` injected directly; no rule | Only with real in-memory Room |
| `RemoteDataSourceImpl` | `MainDispatcherRule()` | No |
| `RepositoryImpl` (`core:data`) | `MainDispatcherRule()` | No |
| `*UnitOfWorkImpl` / `*Persister` (`core:data`) | `MainDispatcherRule()` | No |
| `UseCase` (`core:domain`) | `MainDispatcherRule()` | No |
| `ViewModel` | `MainDispatcherRule(StandardTestDispatcher())` + `advanceUntilIdle()` | No (unless the VM uses Android APIs directly) |
| Mapper / Validator / Util / params builder | `MainDispatcherRule()` if it suspends | No |

```kotlin
// UseCase / Repository / FHIR StoreImpl: default (Unconfined) dispatcher
@get:Rule
val mainDispatcherRule = MainDispatcherRule()

// ViewModel: always StandardTestDispatcher
@get:Rule
val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

// Room StoreImpl over a TestXxxDao: inject TestDispatcherProvider directly, no rule
sut = ChiefComplaintOptionStoreImpl(testDao, TestDispatcherProvider(UnconfinedTestDispatcher()))
```

Use `@get:Rule` (not `@Rule`) and `@OptIn(ExperimentalCoroutinesApi::class)` on the class.

### Exception tests

Use `@Test(expected = ExceptionClass::class)`, never try/catch with a boolean or `assertFailsWith`.

```kotlin
@Test(expected = JsonSyntaxException::class)
fun `loadObjectFromJson should throw exception when json is malformed`() = runTest {
    // Act & Assert — exception propagates out of runTest
    jsonAssetLoader.loadObjectFromJson(testMalformedJson, object : TypeToken<TestPerson>() {})
}
```

- The annotation goes on the test method.
- The throwing call sits directly under `// Act & Assert — exception propagates out of runTest`.
- No `try { } catch { }`, no boolean flag, no separate assertion.

### Section ordering

ViewModel test file:

```
// ========== INITIALIZATION TESTS ==========   ← all init() scenarios
// ========== LOAD TESTS ==========             ← only if load is separate from init
// ========== INTERACTION TESTS ==========      ← one group per onChange/onSelect handler
// ========== VALIDATION TESTS ==========       ← guards, error emissions for bad input
// ========== <ACTION NAME> TESTS ==========    ← e.g. UPSERT PATIENT TESTS, LOGIN TESTS;
//                                                  one section per save/submit action
// ========== HELPER METHODS ==========         ← always last
```

Store, datasource, repository, unit-of-work and use case test files:

```
// ========== <METHOD NAME> TESTS ==========    ← one section per public method
// ========== HELPER METHODS ==========         ← always last
```

Each section tests one method or action. Within a section: happy path, then variations and edge
cases, then error and failure cases.

---

## Persistence and network

### Room DAO (core:database)

DAO tests live in `core/database/src/test/java/org/eha/database/dao/` and test the SQL directly
against a real in-memory Room database under Robolectric. DAOs run synchronously via
`allowMainThreadQueries()`: no `runTest`, no `MainDispatcherRule`, no
`@OptIn(ExperimentalCoroutinesApi::class)`.

```kotlin
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LabTestOptionDaoTest {

    private lateinit var database: EHACareDatabase
    private lateinit var dao: LabTestOptionDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EHACareDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = database.labTestOptionDao()
    }

    @After
    fun tearDown() {
        database.close()  // always in @After
    }

    // ========== getAllOptions TESTS ==========

    @Test
    fun `getAllOptions should return empty list when table is empty`() {
        // Arrange — table starts empty

        // Act
        val result = dao.getAllOptions()

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun `getAllOptions should return all active mobile-compatible entities when inserted`() {
        // Arrange
        dao.insertAll(listOf(testLabTestOptionEntity(loincCode = "2345-7", name = "Glucose")))

        // Act
        val result = dao.getAllOptions()

        // Assert
        assertThat(result).hasSize(1)
    }
}

```
### Room StoreImpl (core:database)

Room-backed stores (`core/database/src/test/java/org/eha/database/store/<name>/`) that inject DAO
interfaces are tested with the `TestXxxDao` fakes from `core:testing`
(`org.eha.testing.fake.database.dao`: `TestChiefComplaintOptionDao`, `TestDiagnosisOptionDao`,
`TestLabTestOptionDao`, `TestLabTestPanelOptionDao`). No Robolectric and no `MainDispatcherRule`:
inject `TestDispatcherProvider(UnconfinedTestDispatcher())` directly.

A store that also takes `EHACareDatabase` (e.g. `LabTestOptionStoreImpl`, whose `replaceAll` runs
in a Room transaction) is tested with real in-memory Room under Robolectric, like a DAO test, with
`database.close()` in `@After`.

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class ChiefComplaintOptionStoreImplTest {

    private lateinit var dao: TestChiefComplaintOptionDao
    private lateinit var sut: ChiefComplaintOptionStoreImpl

    @Before
    fun setup() {
        dao = TestChiefComplaintOptionDao()
        sut = ChiefComplaintOptionStoreImpl(dao, TestDispatcherProvider(UnconfinedTestDispatcher()))
    }

    // No @After needed — in-memory fake, nothing to close

    // ========== getOptions TESTS ==========

    @Test
    fun `getOptions should return matching options when query matches name`() = runTest {
        // Arrange
        dao.insertAll(listOf(entity(1, "Fever", "R50.9"), entity(2, "Cough", "R05")))

        // Act
        val result = sut.getOptions("fev")

        // Assert
        assertThat(result.map { it.id }).containsExactly(1)
    }

    // ========== HELPER METHODS ==========

    private fun entity(id: Int, name: String, icd10Code: String) = /* ChiefComplaintOptionEntity */
}
```

### RemoteDataSourceImpl (MockWebServer)

A real Retrofit instance pointed at `MockWebServer`; no Mockk on the API service.

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class AuthRemoteDataSourceImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var dataSource: AuthRemoteDataSourceImpl

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dataSource = AuthRemoteDataSourceImpl(
            apiService = retrofit.create(AuthApiService::class.java),
            practitionerApiService = retrofit.create(PractitionerApiService::class.java),
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    // ========== LOGIN TESTS ==========

    @Test
    fun `login should return practitioner on 200 response`() = runTest {
        // Arrange
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200).setBody(loginSuccessJson),
        )

        // Act
        val result = dataSource.login("user@example.com", "password")

        // Assert
        assertThat(result).isNotNull()
    }
}
```

---

## Data layer

### RepositoryImpl (fake stores)

Use `TestXxxStore` fakes (`org.eha.testing.fake.fhir.store` / `org.eha.testing.fake.database.store`)
and/or `TestXxxRemoteDataSource` fakes. No Mockk on store or datasource interfaces and no real
FhirEngine: persistence round trips belong in the `XxxStoreImplTest`.

Repositories that open a transaction (same-aggregate multi-resource saves such as
`saveGuidedEncounterFinalization`, `saveEncounterObservations` or `syncLabTests`) take a
`TestTransactionRunner`. It is pass-through with no real rollback, so assert the store writes
made, that each persistence guard holds (for example a clinician-authored resource in the fake
store is not updated by an AI save), and that the practitioner is required only on create; do
not claim rollback.

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class PatientRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testStore: TestPatientStore
    private lateinit var repository: PatientRepositoryImpl

    @Before
    fun setup() {
        testStore = TestPatientStore()
        repository = PatientRepositoryImpl(
            logger = TestAppLogger(),
            patientStore = testStore,
            practitionerManager = TestPractitionerManager(),
        )
    }

    // ========== CREATE PATIENT TESTS ==========

    @Test
    fun `createPatient should return Success with patient ID`() = runTest {
        // Arrange
        val params = testUpsertPatientParams(firstName = "Alice")

        // Act
        val result = repository.createPatient(params)

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isNotNull()
    }

    @Test
    fun `createPatient should map all UpsertPatientParams fields to store request`() =
        runTest {
            // Arrange
            val params = testUpsertPatientParams(firstName = "Bob", lastName = "Brown")

            // Act
            repository.createPatient(params)

            // Assert — the fake captures the request the repository built
            assertThat(testStore.lastCreateRequest?.firstName).isEqualTo("Bob")
        }

    @Test
    fun `createPatient should return Error when store throws`() = runTest {
        // Arrange
        testStore.shouldThrowOnCreate = true

        // Act
        val result = repository.createPatient(testUpsertPatientParams())

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
    }
}
```

### UnitOfWorkImpl and Persister (fake stores)

A cross-aggregate `<Thing>UnitOfWorkImpl` (`org.eha.data.unitofwork.<area>`) and the shared
`*Persister`s (`org.eha.data.persister.<context>`) are tested over `TestXxxStore` fakes, with the
real `*Persister` built over the same fakes and a `TestTransactionRunner`. The pattern itself is
described in `ATOMIC-WRITES.md` in the eha-module-architecture skill (`docs/architecture/ATOMIC-WRITES.md`).

Cover:

- **Each params shape** and its write order. For `EncounterDecisionsUnitOfWork`: decisions only;
  `DRAFT` writes flag, summary, decisions; `COMPLETE` writes summary, decisions, then finishes the
  encounter.
- **Short-circuit.** Make an early write throw (`shouldThrowOnX` / `shouldThrowOnUpdateWhen`) and
  assert the later stores were not called and `commit` returns `Resource.Error`.
- **Practitioner.** Facility and practitioner are stamped on creates; a create with no practitioner
  returns `DataError.UNKNOWN`; updates and deletes succeed without one.
- **Persister.** The sequence itself (diagnosis links kept or rewritten on create/delete; panel
  report append, sub-result removal, order deleted with its results). See
  `EncounterPersisterTest` and `LabTestPersisterTest`.

**Known gap:** the pass-through runner cannot roll back, so these tests do not prove atomicity.
The target is a Robolectric + real-FhirEngine rollback test (the last write fails, no earlier
resource exists); it is blocked while the `core:fhir` store impls and `FhirTransactionRunner` are
`internal`. Keep the `// TODO` on the test class and do not claim rollback is covered.

```kotlin
// Condensed from EncounterDecisionsUnitOfWorkImplTest
// TODO prove rollback against a real FhirEngine once core:data tests can reach the store impls
@OptIn(ExperimentalCoroutinesApi::class)
class EncounterDecisionsUnitOfWorkImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        encounterStore = TestEncounterStore()
        compositionStore = TestCompositionStore()
        conditionStore = TestConditionStore()
        carePlanStore = TestCarePlanStore()
        practitionerManager = TestPractitionerManager()
        practitionerManager.setPractitioner(testPractitioner(id = "practitioner-789"))
        unitOfWork = EncounterDecisionsUnitOfWorkImpl(
            encounterStore = encounterStore,
            compositionStore = compositionStore,
            conditionStore = conditionStore,
            carePlanStore = carePlanStore,
            encounterPersister = EncounterPersister(conditionStore, encounterStore),
            labTestPersister = LabTestPersister(
                TestServiceRequestStore(), TestDiagnosticReportStore(), TestObservationStore(),
            ),
            practitionerManager = practitionerManager,
            transactionRunner = TestTransactionRunner(),
        )
        encounterStore.addEncounter(testFhirEncounter(id = ENCOUNTER_ID))
    }

    // ========== COMMIT TESTS ==========

    @Test
    fun `commit should not write anything else when the drafted flag fails`() = runTest {
        // Arrange — the flag goes first, so a failure must leave no resource behind
        encounterStore.shouldThrowOnUpdateWhen = { it.reviewDrafted != null }
        val params = reviewParams(GuidedEncounterReviewOutcome.DRAFT)

        // Act
        val result = unitOfWork.commit(params)

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat(compositionStore.createCallCount).isEqualTo(0)
        assertThat(carePlanStore.createCallCount).isEqualTo(0)
    }
}
```

---

## Domain and presentation

### UseCase (fake repository or unit of work)

Use `TestXxxRepository` and/or `TestXxxUnitOfWork` (`org.eha.testing.fake.domain.unitofwork.<area>`).
Only orchestrating use cases exist, so every use case test asserts the orchestration:

- guards that short-circuit with `DataError.UNKNOWN`;
- the remote call running before the write, and the write skipped when it fails;
- a remote error passed through unchanged;
- a failed local save mapped to `DataError.UNKNOWN`;
- the **params** handed to the single write call (`TestEncounterDecisionsUnitOfWork.lastParams` /
  `commitCount`, or the repository fake's `lastReceivedX`).

Use cases never open transactions, so their tests never take `TestTransactionRunner`. Params test
data comes from `org.eha.testing.factory.model.<context>` (`testSaveEncounterDecisionsParams()`,
`testSyncLabTestsParams()`, ...). Pure params builders in `org.eha.domain.mapper.<context>` get
plain JVM tests with no fakes.

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class CompleteManualEncounterUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testEncounterRepository: TestEncounterRepository
    private lateinit var testReferralRepository: TestReferralRepository
    private lateinit var useCase: CompleteManualEncounterUseCase

    @Before
    fun setup() {
        testEncounterRepository = TestEncounterRepository()
        testReferralRepository = TestReferralRepository()
        useCase = CompleteManualEncounterUseCase(
            referralRepository = testReferralRepository,
            encounterRepository = testEncounterRepository,
        )
        testEncounterRepository.addEncounter(testEncounterModel(id = "enc-1"))
    }

    // ========== INVOKE TESTS ==========

    @Test
    fun `invoke should submit the referral and mark the encounter complete`() = runTest {
        // Arrange
        testReferralRepository.addReferral(testReferralModel(id = "referral-chew"))

        // Act
        val result = useCase("enc-1", "referral-chew")

        // Assert
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(testReferralRepository.submittedReferralIds).containsExactly("referral-chew")
    }

    @Test
    fun `invoke should not complete the encounter when the submit fails`() = runTest {
        // Arrange
        testReferralRepository.addReferral(testReferralModel(id = "referral-chew"))
        testReferralRepository.shouldReturnErrorOnSubmit = true

        // Act
        val result = useCase("enc-1", "referral-chew")

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
    }
}
```

### ViewModel (fake repositories)

ViewModels inject repository and unit-of-work interfaces directly, so construct them with
`TestXxxRepository` / `TestXxxUnitOfWork` fakes. If the ViewModel uses an orchestrating use case,
build the **real** use case over the fakes, never a mock:

```kotlin
@Before
fun setup() {
    fakeRepository = TestEncounterRepository()
    finalizeUseCase = FinalizeGuidedEncounterUseCase(fakeRepository)  // real use case
    practitionerManager = TestPractitionerManager()
}
```

Use `MainDispatcherRule(StandardTestDispatcher())` and call `advanceUntilIdle()` after every async
action. No `@RunWith(RobolectricTestRunner::class)` unless the ViewModel uses Android framework
APIs directly.

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class PatientListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var testRepository: TestPatientRepository
    private lateinit var viewModel: PatientListViewModel

    @Before
    fun setup() {
        testRepository = TestPatientRepository()
    }

    // ========== INITIALIZATION TESTS ==========

    @Test
    fun `init should load and group patients from repository with section headers`() = runTest {
        // Arrange
        addPatient("Alice", "Anderson")
        addPatient("Bob", "Brown")
        viewModel = createViewModel()

        // Act
        viewModel.init()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.loadState).isEqualTo(PatientListLoadState.Loaded)
        assertThat(state.patients.filterIsInstance<PatientListItem.Patient>()).hasSize(2)
    }

    @Test
    fun `init should show error when repository observe fails`() = runTest {
        // Arrange
        testRepository.shouldReturnErrorOnObserve = true
        viewModel = createViewModel()

        // Act & Assert
        viewModel.uiState.test {
            assertThat(awaitItem().loadState).isEqualTo(PatientListLoadState.Idle)
            viewModel.init()
            assertThat(awaitItem().loadState).isEqualTo(PatientListLoadState.Loading)
            val errorState = awaitItem()
            assertThat(errorState.loadState).isInstanceOf(PatientListLoadState.Error::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ========== INTERACTION TESTS ==========

    @Test
    fun `onSearchTextChange should update query without triggering search`() = runTest {
        // Arrange
        viewModel = createViewModel()
        viewModel.init()
        advanceUntilIdle()

        // Act
        viewModel.onSearchTextChange("John")

        // Assert
        assertThat(viewModel.uiState.value.searchQuery).isEqualTo("John")
    }

    // ========== SEARCH TESTS ==========

    @Test
    fun `search should filter patients matching the query by name or phone`() = runTest {
        // Arrange
        addPatient("John", "Doe")
        addPatient("Jane", "Smith")
        viewModel = createViewModel()
        viewModel.init()
        advanceUntilIdle()

        // Act
        viewModel.onSearchTextChange("doe")
        viewModel.onSearch()
        advanceUntilIdle()

        // Assert
        val patients = viewModel.uiState.value.patients
            .filterIsInstance<PatientListItem.Patient>()
        assertThat(patients).hasSize(1)
        assertThat(patients.first().patient.fullName).contains("Doe")
    }

    // ========== HELPER METHODS ==========

    private fun createViewModel() = PatientListViewModel(
        patientRepository = testRepository,
    )

    // A seed helper (not a model builder): it seeds through the fake's public API
    private suspend fun addPatient(firstName: String, lastName: String): String {
        val params = testUpsertPatientParams(firstName = firstName, lastName = lastName)
        return (testRepository.createPatient(params) as Resource.Success).data
            ?: error("Failed to create patient")
    }
}
```

### Turbine flow testing

```kotlin
// StateFlow — collect the current state and its transitions
viewModel.uiState.test {
    assertThat(awaitItem().loadState).isEqualTo(Idle)
    viewModel.init()
    assertThat(awaitItem().loadState).isEqualTo(Loading)
    assertThat(awaitItem().loadState).isEqualTo(Loaded)
    cancelAndIgnoreRemainingEvents()
}

// SharedFlow (uiEvent) — trigger the action, then collect
viewModel.uiEvent.test {
    viewModel.onLogin()
    advanceUntilIdle()
    val event = awaitItem()
    assertThat(event).isEqualTo(LoginUiEvent.LoginSuccess)
    cancelAndIgnoreRemainingEvents()
}
```

- With `StandardTestDispatcher`, call `advanceUntilIdle()` after any async action before asserting.
- For concurrency tests use `runCurrent()` to advance only to the first suspension point.
- To skip the initial `StateFlow` value, use `skipItems(1)`, then drive the action inside the
  `test {}` block.

---

## Test doubles

Every double is a hand-written `TestXxx` class in `core:testing` under `org.eha.testing.fake.<layer>`,
in the package mirroring the faked interface. When you add a repository, unit-of-work, store,
datasource or manager interface, add its fake there. Check
`core/testing/src/main/java/org/eha/testing/fake/` for an existing one first.

### Double shape

```kotlin
class TestEncounterRepository : EncounterRepository {

    var shouldReturnErrorOnQueryGuidedEncounterAi = false
    var queryGuidedEncounterAiErrorType: DataError = DataError.UNKNOWN

    /** Number of times [queryGuidedEncounterAi] has been called. */
    var queryCallCount = 0

    /** The query string received on the most recent call. */
    var lastReceivedQuery: String? = null

    private var nextResult: GuidedEncounterAiResult = /* ... */

    fun setNextResult(result: GuidedEncounterAiResult) {
        nextResult = result
    }

    override suspend fun queryGuidedEncounterAi(
        encounterId: String,
        param: GuidedEncounterQueryParam,
    ): Resource<GuidedEncounterAiResult, DataError> {
        queryCallCount++
        lastReceivedQuery = param.query
        if (shouldReturnErrorOnQueryGuidedEncounterAi) {
            return Resource.Error(queryGuidedEncounterAiErrorType)
        }
        return Resource.Success(nextResult)
    }

    fun reset() { /* restore every field to its default */ }
}
```

- Error toggles per operation: `shouldReturnErrorOnX` plus `xErrorType`
  (`shouldReturnErrorOnCreate` / `Update` / `Get` / `Delete`), not one global flag.
- Back CRUD fakes with an in-memory map so writes are visible through reads; tests seed state
  through the fake's public API (`fakeRepository.createPatient(params)`), not by poking fields.
- Spies are `var lastReceivedX` / `var xCallCount`, with `private set` where the test only reads.
- Provide `reset()`.
- Default to the happy path, so a test that does not care about a step needs no setup.

### Repository double

A generic template for a new repository's fake
(`core/testing/src/main/java/org/eha/testing/fake/domain/repository/{context}/Test{Name}Repository.kt`):

```kotlin
package org.eha.testing.fake.domain.repository.{context}

import org.eha.domain.repository.{context}.{Name}Repository
import org.eha.model.Resource
import org.eha.model.error.DataError
import org.eha.model.{context}.{Name}Model

class Test{Name}Repository : {Name}Repository {

    private val items = mutableMapOf<String, {Name}Model>()

    var shouldReturnErrorOnGet = false
    var getErrorType: DataError = DataError.UNKNOWN

    override suspend fun get{Name}(id: String): Resource<{Name}Model, DataError> {
        if (shouldReturnErrorOnGet) return Resource.Error(getErrorType)
        return items[id]?.let { Resource.Success(it) } ?: Resource.Error(DataError.UNKNOWN)
    }

    fun add{Name}(item: {Name}Model) {
        items[item.id] = item
    }

    fun reset() {
        items.clear()
        shouldReturnErrorOnGet = false
        getErrorType = DataError.UNKNOWN
    }
}
```

A Flow-backed example, modelled on `TestPatientRepository`:

```kotlin
class TestPatientRepository : PatientRepository {

    private val patients = MutableStateFlow<Map<String, PatientModel>>(emptyMap())

    var shouldReturnErrorOnObserve = false
    var createPatientDelay = 0L

    override suspend fun createPatient(params: UpsertPatientParams): Resource<String?, DataError> {
        if (!params.patientId.isNullOrBlank()) return Resource.Success(null)
        delay(createPatientDelay)
        val id = UUID.randomUUID().toString()
        patients.value += (id to params.toPatientModel(id))
        return Resource.Success(id)
    }

    override fun observePatients(
        params: PatientSearchParam,
    ): Flow<Resource<List<PatientModel>, DataError>> =
        patients.map { map ->
            if (shouldReturnErrorOnObserve) {
                Resource.Error(DataError.UNKNOWN)
            } else {
                Resource.Success(applyFilters(map.values.toList(), params))
            }
        }.catch { emit(Resource.Error(DataError.UNKNOWN)) }

    fun addPatient(patient: PatientModel) { patients.value += (patient.id to patient) }
}
```

### Unit-of-work double

`TestXxxUnitOfWork` (`fake/domain/unitofwork/<area>/`) implements the one `commit` function: it
records the received params and call count and returns a configurable result. It persists
nothing; assert on the params the use case or ViewModel built.

```kotlin
// core/testing/.../fake/domain/unitofwork/encounter/TestEncounterDecisionsUnitOfWork.kt
class TestEncounterDecisionsUnitOfWork : EncounterDecisionsUnitOfWork {

    var result: Resource<SavedClinicianIds, DataError> = Resource.Success(SavedClinicianIds())

    var lastParams: SaveEncounterDecisionsParams? = null
        private set

    var commitCount: Int = 0
        private set

    override suspend fun commit(
        params: SaveEncounterDecisionsParams,
    ): Resource<SavedClinicianIds, DataError> {
        commitCount++
        lastParams = params
        return result
    }
}
```

### Store double

`TestXxxStore` implements the store interface over an in-memory map of FHIR resources (or Room
entities), with `shouldThrowOnX` toggles, `lastXRequest` / `xCallCount` spies and seed helpers.
Add a new one to `fake/fhir/store` or `fake/database/store` when you add a store.

```kotlin
class TestPatientStore : PatientStore {
    private val patients = mutableMapOf<String, Patient>()  // FHIR Patient, not domain

    var shouldThrowOnCreate = false
    var lastCreateRequest: CreatePatientRequest? = null
    var createCallCount = 0

    override suspend fun createPatient(request: CreatePatientRequest): String {
        if (shouldThrowOnCreate) throw ResourceNotFoundException("Patient", "create")
        lastCreateRequest = request
        createCallCount++
        // build a Patient from request, store it, return its id
    }

    fun addPatient(patient: Patient) { patients[patient.idPart] = patient }
}
```

### Existing doubles

All under `core/testing/src/main/java/org/eha/testing/`:

- `rule/MainDispatcherRule`, `application/EHACareTestApplication`
- `fake/domain/repository/auth/`: `TestAuthRepository`; `patient/`: `TestPatientRepository`
- `fake/domain/repository/encounter/`: `TestEncounterRepository` (diagnoses, care plans,
  observations and guided-encounter AI), `TestLabTestRepository`, `TestReferralRepository`,
  `TestPrescriptionRepository`
- `fake/domain/repository/reference/`: `TestLocationRepository`, `TestAdministrativeAreaRepository`,
  `TestChiefComplaintOptionRepository`, `TestDiagnosisOptionRepository`,
  `TestLabTestOptionRepository`, `TestEwsScoringConfigRepository`
- `fake/domain/unitofwork/encounter/`: `TestEncounterDecisionsUnitOfWork`
- `fake/domain/manager/`: `TestSessionManager`, `TestPractitionerManager`,
  `TestAdditionalFacilitiesManager`, `TestSyncCheckpointManager`
- `fake/domain/service/`: `TestFacilityDataPurger`, `TestFhirEngineIndexer`,
  `TestLocalDatabaseWiper`
- `fake/fhir/transaction/`: `TestTransactionRunner` (pass-through, no real rollback, exposes
  `invocationCount`; for `RepositoryImpl` / `*UnitOfWorkImpl` tests, never use cases)
- `fake/fhir/store/`: `TestPatientStore`, `TestEncounterStore`, `TestObservationStore`,
  `TestConditionStore`, `TestCarePlanStore`, `TestLocationStore`, `TestServiceRequestStore`,
  `TestDiagnosticReportStore`, `TestMedicationStore`, `TestMedicationRequestStore`,
  `TestCompositionStore`; `fake/fhir/FailingGetFhirEngine`
- `fake/database/store/`: `TestChiefComplaintOptionStore`, `TestDiagnosisOptionStore`,
  `TestLabTestOptionStore`; `fake/database/dao/`: `TestChiefComplaintOptionDao`,
  `TestDiagnosisOptionDao`, `TestLabTestOptionDao`, `TestLabTestPanelOptionDao`
- `fake/data/datasource/`: `TestAuthRemoteDataSource`, `TestChiefComplaintOptionRemoteDataSource`,
  `TestDiagnosisOptionRemoteDataSource`, `TestLabTestOptionRemoteDataSource`,
  `TestReferralRemoteDataSource`, `TestEncounterAiRemoteDataSource`,
  `TestEwsScoringConfigRemoteDataSource`; `fake/data/store/TestEwsScoringConfigStore`
- `fake/common/` (`TestAppLogger`, `TestNetworkMonitor`, `TestDispatcherProvider`),
  `fake/security/` (`TestCrypto`, `TestPasswordHasher`), `fake/auth/` (`TestTokenManager`,
  `TestCredentialManager`), `fake/cache/` (`TestAppCache`, `CountingAppCache`),
  `fake/config/TestConfigProvider`, `fake/sync/` (`TestEHACareFhirSyncManagerImpl`,
  `TestSyncOperationsProvider`)

---

## Test data factories

Build test data with the top-level factory functions in `core:testing`
(`org.eha.testing.factory.{model/<context>,fhir,fhir/request,dto,entity,ui}`), never with local
builder functions in the test file. The rules are summarised in
[TESTING.md](TESTING.md#test-data-factories); in practice:

```kotlin
// Pass every value you assert on
val patient = testPatientModel(firstName = "Ada")
testRepository.addPatient(patient)
// ...
assertThat(state.patients.first().firstName).isEqualTo("Ada")
```

- One `<ClassName>TestFactory.kt` per class (FHIR resources prefixed `Fhir`, e.g.
  `FhirPatientTestFactory.kt`) exposing `test<ClassName>(...)` with a fixed deterministic default
  for every parameter: `testPatientModel(...)`, `testFhirPatient(...)`, `testLabTestOptionDto(...)`.
- Variant builders are `test<Scenario><ClassName>()` (`testDiagnosisGuidedEncounterAiResult()`).
- When a production type gains a required parameter, update its factory once with a sensible
  default rather than touching each test.
- A new model, DTO, entity or FHIR type constructed in more than one test gets a factory in
  `core:testing`.
- Factories for presentation-only types (`*Ui`, field state) live in the feature's own
  `src/test/java/.../factory/`, one file per class, with the same naming.

---

## Checklist

- [ ] File named `{ClassName}Test.kt`; no `UnitTest` / `IntegrationTest` suffix.
- [ ] Approach matches the layer ([TESTING.md](TESTING.md#layer-to-approach)).
- [ ] DAO test: real in-memory Room + Robolectric; synchronous (no `runTest`, no
      `MainDispatcherRule`); `database.close()` in `@After`.
- [ ] Room StoreImpl: `TestXxxDao` fakes (real Room if the store takes `EHACareDatabase`);
      `TestDispatcherProvider(UnconfinedTestDispatcher())` injected directly.
- [ ] FHIR StoreImpl: real FhirEngine + Robolectric; `FhirEngineProvider.cleanup()` in `@After`.
- [ ] RemoteDataSourceImpl: `MockWebServer` started in `@Before`, shut down in `@After`.
- [ ] UnitOfWorkImpl / Persister: fake stores + `TestTransactionRunner`; write order per params
      shape, short-circuit on a failing write, practitioner on create; rollback not claimed
      (`// TODO` kept).
- [ ] UseCase: asserts the params passed to the single write call; no `TestTransactionRunner`.
- [ ] ViewModel: `MainDispatcherRule(StandardTestDispatcher())` and `advanceUntilIdle()` after
      every async action.
- [ ] No Mockk on DAOs, Retrofit services, stores, datasources, repositories, units of work or
      use cases.
- [ ] Exception tests use `@Test(expected = ...)` with `// Act & Assert — exception propagates out of runTest`.
- [ ] Backtick names `<action> should <outcome> when <condition>`; every line under 100 chars.
- [ ] `// Arrange`, `// Act`, `// Assert` in every test.
- [ ] Sections in order (INITIALIZATION, LOAD, INTERACTION, VALIDATION, actions, HELPER METHODS);
      happy path, then variations, then errors.
- [ ] Data from `core:testing` factories; asserted values passed explicitly; no local model
      builders.
- [ ] Doubles from `core:testing` `fake/`; Robolectric uses `EHACareTestApplication`.
- [ ] Truth assertions; Turbine for flows.
- [ ] Seed and SUT helpers at the bottom under `// ========== HELPER METHODS ==========`.
