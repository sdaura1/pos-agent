package com.example.pos_agent.domain.auth.usecase

import com.example.pos_agent.auth.FakeAuthRepository
import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthErrorCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUseCaseTest {

    @Test
    fun `login should reject a blank username when submitted`() = runBlocking {
        // Arrange
        val repository = FakeAuthRepository()
        val useCase = LoginUseCase(repository)

        // Act
        val result = useCase("   ", "secret")

        // Assert
        assertTrue(result is Response.Failure)
        assertTrue((result as Response.Failure).error is DomainError.Validation)
        assertEquals(AuthErrorCode.USERNAME_REQUIRED, result.error.code)
        assertNull(repository.lastCredentials)
    }

    @Test
    fun `login should reject a blank password when submitted`() = runBlocking {
        // Arrange
        val repository = FakeAuthRepository()
        val useCase = LoginUseCase(repository)

        // Act
        val result = useCase("operator", "")

        // Assert
        assertTrue(result is Response.Failure)
        assertEquals(
            AuthErrorCode.PASSWORD_REQUIRED,
            (result as Response.Failure).error.code,
        )
        assertNull(repository.lastCredentials)
    }

    @Test
    fun `login should trim the username when credentials are forwarded`() = runBlocking {
        // Arrange
        val repository = FakeAuthRepository()
        val expected = FakeAuthRepository.session()
        repository.loginResult = Response.Success(expected)
        val useCase = LoginUseCase(repository)

        // Act
        val result = useCase("  operator  ", "secret")

        // Assert
        when (result) {
            is Response.Success -> assertSame(expected, result.value)
            is Response.Failure -> throw AssertionError("Expected success, got $result")
        }
        assertEquals("operator", repository.lastCredentials?.username)
        assertEquals("secret", repository.lastCredentials?.password)
    }

    @Test
    fun `login should pass the password unchanged when submitted`() = runBlocking {
        // Arrange
        val repository = FakeAuthRepository()
        val useCase = LoginUseCase(repository)

        // Act
        useCase("operator", "  spaced  ")

        // Assert
        assertEquals("  spaced  ", repository.lastCredentials?.password)
    }

    @Test
    fun `login should propagate the repository error when authentication fails`() = runBlocking {
        // Arrange
        val error = DomainError.Validation("Invalid username or password.")
        val repository = FakeAuthRepository(Response.Failure(error))
        val useCase = LoginUseCase(repository)

        // Act
        val result = useCase("operator", "wrong")

        // Assert
        assertSame(error, (result as Response.Failure).error)
    }
}
