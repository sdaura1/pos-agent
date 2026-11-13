package com.example.pos_agent.ui.auth

import com.example.pos_agent.core.error.DomainError

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val usernameError: LoginFieldError? = null,
    val passwordError: LoginFieldError? = null,
    val error: DomainError? = null,
    val isLoading: Boolean = false,
){
    val isFormValid: Boolean
        get() = usernameError == null &&
                passwordError == null &&
                username.trim().isNotBlank() &&
                password.trim().isNotBlank()
}
