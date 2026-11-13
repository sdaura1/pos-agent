package com.example.pos_agent.ui.main

import com.example.pos_agent.auth.FakeAuthRepository
import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.usecase.LogoutUseCase
import com.example.pos_agent.domain.auth.usecase.RestoreSessionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val mainDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeAuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        repository = FakeAuthRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = MainViewModel(
        restoreSessionUseCase = RestoreSessionUseCase(repository),
        logoutUseCase = LogoutUseCase(repository),
    )

    @Test
    fun `restores a persisted session`() = runTest(mainDispatcher.scheduler) {
        val session = FakeAuthRepository.session()
        repository.restoreResult = Response.Success(session)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.loadState.value
        assertFalse(state.isLoading)
        assertEquals(AppAuthState.AUTHENTICATED, state.authState)
        assertEquals(session, viewModel.session.value)
    }

    @Test
    fun `no stored session goes to unauthenticated`() = runTest(mainDispatcher.scheduler) {
        repository.restoreResult = Response.Success(null)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.loadState.value
        assertFalse(state.isLoading)
        assertEquals(AppAuthState.UNAUTHENTICATED, state.authState)
        assertNull(viewModel.session.value)
    }

    @Test
    fun `restore failure goes to unauthenticated`() = runTest(mainDispatcher.scheduler) {
        repository.restoreResult = Response.Failure(DomainError.Unknown("read failed"))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.loadState.value
        assertFalse(state.isLoading)
        assertEquals(AppAuthState.UNAUTHENTICATED, state.authState)
        assertNull(viewModel.session.value)
    }

    @Test
    fun `authenticated user updates load state and session`() = runTest(mainDispatcher.scheduler) {
        repository.restoreResult = Response.Success(null)
        val session = FakeAuthRepository.session()

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onAuthenticated(session)

        val state = viewModel.loadState.value
        assertFalse(state.isLoading)
        assertEquals(AppAuthState.AUTHENTICATED, state.authState)
        assertEquals(session, viewModel.session.value)
    }

    @Test
    fun `logout clears the session`() = runTest(mainDispatcher.scheduler) {
        repository.restoreResult = Response.Success(FakeAuthRepository.session())

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.logout()
        advanceUntilIdle()

        val state = viewModel.loadState.value
        assertFalse(state.isLoading)
        assertEquals(AppAuthState.UNAUTHENTICATED, state.authState)
        assertNull(viewModel.session.value)
        assertTrue(repository.logoutCalled)
    }
}
