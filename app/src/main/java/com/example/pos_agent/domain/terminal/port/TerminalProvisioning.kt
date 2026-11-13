package com.example.pos_agent.domain.terminal.port

import com.example.pos_agent.domain.terminal.model.ProvisioningResult
import com.example.pos_agent.domain.terminal.model.ProvisioningState
import kotlinx.coroutines.flow.StateFlow

/**
 * Port for making the terminal cryptographically ready to transact.
 *
 * Implementations own key exchange, parameter download and secure storage.
 * Callers only observe [state] and ask for readiness; they never see key
 * material or ISO 8583 details.
 */
interface TerminalProvisioning {
    val state: StateFlow<ProvisioningState>

    /**
     * Idempotent: returns [ProvisioningResult.Success] when the terminal is
     * provisioned, or the failure that prevented it. Concurrent callers must share
     * a single attempt.
     */
    suspend fun ensureProvisioned(): ProvisioningResult

    /** Forces a fresh attempt, for key rotation or after a failure. */
    suspend fun reprovision(): ProvisioningResult
}
