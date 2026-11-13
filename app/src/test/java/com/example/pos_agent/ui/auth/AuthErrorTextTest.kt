package com.example.pos_agent.ui.auth

import com.example.pos_agent.R
import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.domain.auth.model.AuthErrorCode
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthErrorTextTest {

    @Test
    fun `an invalid credentials error should map to its resource when resolved`() {
        // Arrange
        val error = DomainError.Validation(
            message = "",
            code = AuthErrorCode.INVALID_CREDENTIALS,
        )

        // Act
        val resource = error.toMessageRes()

        // Assert
        assertEquals(R.string.auth_error_invalid_credentials, resource)
    }

    @Test
    fun `a network error should map to its resource when resolved`() {
        // Arrange
        val error = DomainError.Network(
            message = "",
            code = AuthErrorCode.NETWORK,
        )

        // Act
        val resource = error.toMessageRes()

        // Assert
        assertEquals(R.string.auth_error_network, resource)
    }

    @Test
    fun `an unknown error should map to the generic resource when resolved`() {
        // Arrange
        val error = DomainError.Unknown("")

        // Act
        val resource = error.toMessageRes()

        // Assert
        assertEquals(R.string.auth_error_generic, resource)
    }

    @Test
    fun `a username field error should map to its resource when resolved`() {
        // Arrange
        val error = LoginFieldError.USERNAME_REQUIRED

        // Act
        val resource = error.toMessageRes()

        // Assert
        assertEquals(R.string.login_error_username_required, resource)
    }

    @Test
    fun `a password field error should map to its resource when resolved`() {
        // Arrange
        val error = LoginFieldError.PASSWORD_REQUIRED

        // Act
        val resource = error.toMessageRes()

        // Assert
        assertEquals(R.string.login_error_password_required, resource)
    }
}
