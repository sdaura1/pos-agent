package com.example.pos_agent.ui.auth

import com.example.pos_agent.R
import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.domain.auth.model.AuthErrorCode
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthErrorTextTest {

    @Test
    fun `invalid credentials maps to its string resource`() {
        val error = DomainError.Validation(
            message = "",
            code = AuthErrorCode.INVALID_CREDENTIALS,
        )

        assertEquals(R.string.auth_error_invalid_credentials, error.toMessageRes())
    }

    @Test
    fun `network error maps to its string resource`() {
        val error = DomainError.Network(
            message = "",
            code = AuthErrorCode.NETWORK,
        )

        assertEquals(R.string.auth_error_network, error.toMessageRes())
    }

    @Test
    fun `unknown error maps to the generic resource`() {
        val error = DomainError.Unknown("")

        assertEquals(R.string.auth_error_generic, error.toMessageRes())
    }

    @Test
    fun `username field error maps to its string resource`() {
        assertEquals(
            R.string.login_error_username_required,
            LoginFieldError.USERNAME_REQUIRED.toMessageRes(),
        )
    }

    @Test
    fun `password field error maps to its string resource`() {
        assertEquals(
            R.string.login_error_password_required,
            LoginFieldError.PASSWORD_REQUIRED.toMessageRes(),
        )
    }
}
