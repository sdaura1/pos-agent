package com.example.pos_agent.ui.auth

import androidx.annotation.StringRes
import com.example.pos_agent.R
import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.domain.auth.model.AuthErrorCode

@StringRes
fun DomainError.toMessageRes(): Int = when (code) {
    AuthErrorCode.INVALID_CREDENTIALS -> R.string.auth_error_invalid_credentials
    AuthErrorCode.ACCOUNT_LOCKED -> R.string.auth_error_account_locked
    AuthErrorCode.TOO_MANY_ATTEMPTS -> R.string.auth_error_too_many_attempts
    AuthErrorCode.NETWORK -> R.string.auth_error_network
    AuthErrorCode.INVALID_RESPONSE -> R.string.auth_error_invalid_response
    AuthErrorCode.USERNAME_REQUIRED -> R.string.login_error_username_required
    AuthErrorCode.PASSWORD_REQUIRED -> R.string.login_error_password_required
    else -> R.string.auth_error_generic
}

@StringRes
fun LoginFieldError.toMessageRes(): Int = when (this) {
    LoginFieldError.USERNAME_REQUIRED -> R.string.login_error_username_required
    LoginFieldError.PASSWORD_REQUIRED -> R.string.login_error_password_required
}
