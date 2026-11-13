package com.example.pos_agent.data.auth.remote.mapper

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.domain.auth.model.AuthErrorCode
import retrofit2.HttpException

/**
 * Maps auth-service HTTP failures to domain errors with stable UI codes.
 * The `message` values are for logs; user-facing text is resolved in the UI.
 */
object AuthErrorMapper {
    fun fromHttp(error: HttpException): DomainError = when (error.code()) {
        401, 403 -> DomainError.Validation(
            message = "Invalid credentials",
            code = AuthErrorCode.INVALID_CREDENTIALS,
        )

        423 -> DomainError.Conflict(
            message = "Account locked",
            code = AuthErrorCode.ACCOUNT_LOCKED,
        )

        429 -> DomainError.Conflict(
            message = "Too many sign-in attempts",
            code = AuthErrorCode.TOO_MANY_ATTEMPTS,
        )

        else -> DomainError.Unknown(
            message = "Sign-in failed (HTTP ${error.code()})",
            cause = error,
            code = AuthErrorCode.SERVER,
        )
    }
}
