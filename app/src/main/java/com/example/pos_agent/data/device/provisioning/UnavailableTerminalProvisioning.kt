package com.example.pos_agent.data.device.provisioning

import com.example.pos_agent.domain.terminal.model.ProvisioningFailureReason
import com.example.pos_agent.domain.terminal.model.ProvisioningResult
import com.example.pos_agent.domain.terminal.model.ProvisioningState
import com.example.pos_agent.domain.terminal.model.ProvisioningStateMachine
import com.example.pos_agent.domain.terminal.port.TerminalProvisioning
import kotlinx.coroutines.flow.StateFlow

/**
 * Safe default until the Kozen adapter exists.
 *
 * It reports unavailable instead of pretending to be provisioned, so an app
 * built without a provisioning adapter can never start a card transaction.
 */
class UnavailableTerminalProvisioning : TerminalProvisioning {

    private val machine = ProvisioningStateMachine()

    override val state: StateFlow<ProvisioningState> = machine.state

    override suspend fun ensureProvisioned(): ProvisioningResult = fail()

    override suspend fun reprovision(): ProvisioningResult = fail()

    private fun fail(): ProvisioningResult {
        machine.markFailed(
            ProvisioningFailureReason.SERVICE_UNAVAILABLE,
            "No terminal provisioning adapter is installed",
        )
        return ProvisioningResult.Failure(
            ProvisioningFailureReason.SERVICE_UNAVAILABLE,
            "No terminal provisioning adapter is installed",
        )
    }
}
