package com.example.pos_agent.domain.terminal.model

/** Result of asking the terminal to become provisioned. */
sealed interface ProvisioningResult {
    data object Success : ProvisioningResult

    data class Failure(
        val reason: ProvisioningFailureReason,
        val detail: String? = null,
    ) : ProvisioningResult
}
