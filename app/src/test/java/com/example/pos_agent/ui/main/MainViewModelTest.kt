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

    @Test
    fun `restore should authenticate when a session is stored`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            val session = FakeAuthRepository.session()
            repository.restoreResult = Response.Success(session)

            // Act
            val viewModel = createViewModel()
            advanceUntilIdle()

            // Assert
            val state = viewModel.loadState.value
            assertFalse(state.isLoading)
            assertEquals(AppAuthState.AUTHENTICATED, state.authState)
            assertEquals(session, viewModel.session.value)
        }

    @Test
    fun `restore should go unauthenticated when no session is stored`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            repository.restoreResult = Response.Success(null)

            // Act
            val viewModel = createViewModel()
            advanceUntilIdle()

            // Assert
            val state = viewModel.loadState.value
            assertFalse(state.isLoading)
            assertEquals(AppAuthState.UNAUTHENTICATED, state.authState)
            assertNull(viewModel.session.value)
        }

    @Test
    fun `restore should go unauthenticated when the read fails`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            repository.restoreResult = Response.Failure(
                DomainError.Unknown("read failed"),
            )

            // Act
            val viewModel = createViewModel()
            advanceUntilIdle()

            // Assert
            val state = viewModel.loadState.value
            assertFalse(state.isLoading)
            assertEquals(AppAuthState.UNAUTHENTICATED, state.authState)
            assertNull(viewModel.session.value)
        }

    @Test
    fun `onAuthenticated should update state and session when signed in`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            repository.restoreResult = Response.Success(null)
            val session = FakeAuthRepository.session()
            val viewModel = createViewModel()
            advanceUntilIdle()

            // Act
            viewModel.onAuthenticated(session)

            // Assert
            val state = viewModel.loadState.value
            assertFalse(state.isLoading)
            assertEquals(AppAuthState.AUTHENTICATED, state.authState)
            assertEquals(session, viewModel.session.value)
        }

    @Test
    fun `logout should clear the session when requested`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            repository.restoreResult = Response.Success(
                FakeAuthRepository.session(),
            )
            val viewModel = createViewModel()
            advanceUntilIdle()

            // Act
            viewModel.logout()
            advanceUntilIdle()

            // Assert
            val state = viewModel.loadState.value
            assertFalse(state.isLoading)
            assertEquals(AppAuthState.UNAUTHENTICATED, state.authState)
            assertNull(viewModel.session.value)
            assertTrue(repository.logoutCalled)
        }

    private fun createViewModel() = MainViewModel(
        restoreSessionUseCase = RestoreSessionUseCase(repository),
        logoutUseCase = LogoutUseCase(repository),
    )
}
