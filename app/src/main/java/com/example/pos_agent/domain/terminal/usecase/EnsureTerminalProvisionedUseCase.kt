package com.example.pos_agent.domain.terminal.usecase

import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.terminal.model.ProvisioningResult
import com.example.pos_agent.domain.terminal.model.toDomainError
import com.example.pos_agent.domain.terminal.port.TerminalProvisioning
import javax.inject.Inject

/**
 * Gate every card operation on a provisioned terminal.
 *
 * Callers must not start card acquisition when this fails. The failure carries
 * a stable [com.example.pos_agent.domain.terminal.model.ProvisioningErrorCode]
 * so the UI can explain what is missing.
 */
class EnsureTerminalProvisionedUseCase @Inject constructor(
    private val provisioning: TerminalProvisioning,
) {
    suspend operator fun invoke(): Response<Unit> = when (val outcome = provisioning.ensureProvisioned()) {
        ProvisioningResult.Success -> Response.Success(Unit)
        is ProvisioningResult.Failure -> Response.Failure(
            outcome.reason.toDomainError(outcome.detail)
        )
    }
}
