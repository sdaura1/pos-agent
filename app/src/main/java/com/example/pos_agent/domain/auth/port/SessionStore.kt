package com.example.pos_agent.domain.auth.port

import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthSession

/**
 * Durable session storage. The data layer encrypts tokens before writing.
 */
interface SessionStore {
    suspend fun read(): Response<AuthSession?>

    suspend fun write(session: AuthSession): Response<Unit>

    suspend fun clear(): Response<Unit>
}
