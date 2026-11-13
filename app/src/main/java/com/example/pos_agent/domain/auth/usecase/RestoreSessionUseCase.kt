package com.example.pos_agent.domain.auth.usecase

import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.port.AuthRepository
import javax.inject.Inject

/** Restores a persisted session so the user stays signed in across launches. */
class RestoreSessionUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Response<AuthSession?> = repository.restoreSession()
}
