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
    fun `blank username is rejected before hitting the repository`() = runBlocking {
        val repository = FakeAuthRepository()
        val result = LoginUseCase(repository)("   ", "secret")

        assertTrue(result is Response.Failure)
        assertTrue((result as Response.Failure).error is DomainError.Validation)
        assertEquals(AuthErrorCode.USERNAME_REQUIRED, result.error.code)
        assertNull(repository.lastCredentials)
    }

    @Test
    fun `blank password is rejected before hitting the repository`() = runBlocking {
        val repository = FakeAuthRepository()
        val result = LoginUseCase(repository)("operator", "")

        assertTrue(result is Response.Failure)
        assertEquals(AuthErrorCode.PASSWORD_REQUIRED, (result as Response.Failure).error.code)
        assertNull(repository.lastCredentials)
    }

    @Test
    fun `username is trimmed and credentials are forwarded`() = runBlocking {
        val repository = FakeAuthRepository()
        val expected = FakeAuthRepository.session()
        repository.loginResult = Response.Success(expected)

        val result = LoginUseCase(repository)("  operator  ", "secret")

        when (result) {
            is Response.Success -> assertSame(expected, result.value)
            is Response.Failure -> throw AssertionError("Expected success, got ${result.error.message}")
        }
        assertEquals("operator", repository.lastCredentials?.username)
        assertEquals("secret", repository.lastCredentials?.password)
    }

    @Test
    fun `password is passed through exactly as entered`() = runBlocking {
        val repository = FakeAuthRepository()

        LoginUseCase(repository)("operator", "  spaced  ")

        assertEquals("  spaced  ", repository.lastCredentials?.password)
    }

    @Test
    fun `repository failure is propagated`() = runBlocking {
        val error = DomainError.Validation("Invalid username or password.")
        val repository = FakeAuthRepository(Response.Failure(error))

        val result = LoginUseCase(repository)("operator", "wrong")

        assertSame(error, (result as Response.Failure).error)
    }
}
