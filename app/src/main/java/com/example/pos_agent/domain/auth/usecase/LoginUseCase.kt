package com.example.pos_agent.domain.auth.usecase

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthErrorCode
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.model.LoginCredentials
import com.example.pos_agent.domain.auth.port.AuthRepository
import javax.inject.Inject

/**
 * Validates the form input, then delegates authentication to the repository.
 *
 * Usernames are trimmed; passwords are used exactly as entered.
 */
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(username: String, password: String): Response<AuthSession> {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isEmpty()) {
            return Response.Failure(
                DomainError.Validation(
                    message = "Username is required",
                    code = AuthErrorCode.USERNAME_REQUIRED,
                )
            )
        }
        if (password.isEmpty()) {
            return Response.Failure(
                DomainError.Validation(
                    message = "Password is required",
                    code = AuthErrorCode.PASSWORD_REQUIRED,
                )
            )
        }
        return repository.login(
            LoginCredentials(
                username = trimmedUsername,
                password = password,
            )
        )
    }
}
