package com.example.pos_agent.domain.auth.model

object AuthErrorCode {
    const val INVALID_CREDENTIALS = "auth.invalid_credentials"
    const val ACCOUNT_LOCKED = "auth.account_locked"
    const val TOO_MANY_ATTEMPTS = "auth.too_many_attempts"
    const val NETWORK = "auth.network"
    const val SERVER = "auth.server"
    const val INVALID_RESPONSE = "auth.invalid_response"
    const val USERNAME_REQUIRED = "auth.username_required"
    const val PASSWORD_REQUIRED = "auth.password_required"
}
