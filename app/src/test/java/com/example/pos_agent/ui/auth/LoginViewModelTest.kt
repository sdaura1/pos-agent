package com.example.pos_agent.ui.auth

import com.example.pos_agent.auth.FakeAuthRepository
import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthErrorCode
import com.example.pos_agent.domain.auth.usecase.LoginUseCase
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
class LoginViewModelTest {

    private val mainDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeAuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        repository = FakeAuthRepository()
        viewModel = LoginViewModel(LoginUseCase(repository))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submit should show field errors when the fields are blank`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            viewModel.onUsernameChange("")
            viewModel.onPasswordChange("")

            // Act
            viewModel.submit()

            // Assert
            val state = viewModel.state.value
            assertEquals(LoginFieldError.USERNAME_REQUIRED, state.usernameError)
            assertEquals(LoginFieldError.PASSWORD_REQUIRED, state.passwordError)
            assertNull(repository.lastCredentials)
        }

    @Test
    fun `editing a field should clear its error when changed`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            viewModel.submit()

            // Act
            viewModel.onUsernameChange("operator")

            // Assert
            assertNull(viewModel.state.value.usernameError)
            assertEquals("operator", viewModel.state.value.username)
        }

    @Test
    fun `a successful sign-in should clear the form when submitted`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            repository.loginResult = Response.Success(FakeAuthRepository.session())
            viewModel.onUsernameChange("operator")
            viewModel.onPasswordChange("secret")

            // Act
            viewModel.submit()
            advanceUntilIdle()

            // Assert
            val state = viewModel.state.value
            assertFalse(state.isSubmitting)
            assertEquals("", state.username)
            assertEquals("", state.password)
            assertNull(state.error)
            assertEquals("operator", repository.lastCredentials?.username)
        }

    @Test
    fun `a failed sign-in should keep the form when submitted`() =
        runTest(mainDispatcher.scheduler) {
            // Arrange
            repository.loginResult = Response.Failure(
                DomainError.Validation(
                    message = "Invalid username or password.",
                    code = AuthErrorCode.INVALID_CREDENTIALS,
                )
            )
            viewModel.onUsernameChange("operator")
            viewModel.onPasswordChange("wrong")

            // Act
            viewModel.submit()
            advanceUntilIdle()

            // Assert
            val state = viewModel.state.value
            assertFalse(state.isSubmitting)
            assertEquals(AuthErrorCode.INVALID_CREDENTIALS, state.error?.code)
            assertEquals("operator", state.username)
            assertEquals("wrong", state.password)
        }

    @Test
    fun `password visibility should toggle when requested`() {
        // Arrange
        val initiallyVisible = viewModel.state.value.isPasswordVisible

        // Act
        viewModel.onTogglePasswordVisibility()

        // Assert
        assertFalse(initiallyVisible)
        assertTrue(viewModel.state.value.isPasswordVisible)
    }
}
