package com.example.pos_agent.domain.terminal.model

/**
 * Where terminal provisioning is in its lifecycle.
 *
 * [Provisioned] is the only state in which a purchase may start. It is reached
 * only after both keys and host parameters have been downloaded successfully.
 */
sealed interface ProvisioningState {
    data object Unprovisioned : ProvisioningState

    data object DownloadingKeys : ProvisioningState

    data object DownloadingParameters : ProvisioningState

    data object Provisioned : ProvisioningState

    data class Failed(
        val reason: ProvisioningFailureReason,
        val detail: String? = null,
    ) : ProvisioningState
}
