package com.example.pos_agent.domain.auth.port

import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.model.LoginCredentials

/**
 * Port for authentication and session persistence. The implementation lives
 * in `data/auth/repository`.
 */
interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Response<AuthSession>

    /** Reads the stored session, or `Success(null)` when none is valid. */
    suspend fun restoreSession(): Response<AuthSession?>

    suspend fun logout(): Response<Unit>
}
