package com.example.pos_agent.domain.auth.usecase

import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.port.AuthRepository
import javax.inject.Inject

/** Clears the persisted session and returns the app to the login screen. */
class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Response<Unit> = repository.logout()
}
