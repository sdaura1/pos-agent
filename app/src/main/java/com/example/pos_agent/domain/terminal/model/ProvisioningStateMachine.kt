package com.example.pos_agent.domain.terminal.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Owns the provisioning order and rejects transitions that skip steps.
 *
 * Adapters perform the I/O and report progress here; they must not jump
 * straight to [ProvisioningState.Provisioned] after a partial download.
 */
class ProvisioningStateMachine(
    initial: ProvisioningState = ProvisioningState.Unprovisioned,
) {
    private val mutableState = MutableStateFlow(initial)

    val state: StateFlow<ProvisioningState> = mutableState.asStateFlow()

    /** Starts a fresh attempt from unprovisioned, failed or provisioned (refresh). */
    fun beginKeyDownload() {
        val current = mutableState.value
        val allowed = current is ProvisioningState.Unprovisioned ||
            current is ProvisioningState.Failed ||
            current is ProvisioningState.Provisioned
        require(allowed) { "Cannot start key download from $current" }
        mutableState.value = ProvisioningState.DownloadingKeys
    }

    fun beginParameterDownload() {
        val current = mutableState.value
        require(current is ProvisioningState.DownloadingKeys) {
            "Parameters can only follow a key download, was $current"
        }
        mutableState.value = ProvisioningState.DownloadingParameters
    }

    fun markProvisioned() {
        val current = mutableState.value
        require(current is ProvisioningState.DownloadingParameters) {
            "Provisioning can only complete after parameter download, was $current"
        }
        mutableState.value = ProvisioningState.Provisioned
    }

    /** Records a failure before the first attempt or during any step. */
    fun markFailed(reason: ProvisioningFailureReason, detail: String? = null) {
        val current = mutableState.value
        require(current !is ProvisioningState.Provisioned) {
            "A provisioned terminal cannot fail without a new attempt"
        }
        mutableState.value = ProvisioningState.Failed(reason, detail)
    }

    fun reset() {
        val current = mutableState.value
        require(current is ProvisioningState.Failed) {
            "Only a failed provisioning can be reset, was $current"
        }
        mutableState.value = ProvisioningState.Unprovisioned
    }
}
