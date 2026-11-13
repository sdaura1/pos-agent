package com.example.pos_agent.data.device.fake

import com.example.pos_agent.domain.terminal.model.ProvisioningResult
import com.example.pos_agent.domain.terminal.model.ProvisioningState
import com.example.pos_agent.domain.terminal.model.ProvisioningStateMachine
import com.example.pos_agent.domain.terminal.port.TerminalProvisioning
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Deterministic provisioning double for emulator runs and tests.
 *
 * The default outcome is [ProvisioningResult.Success]. Pass a failure outcome to
 * exercise the unavailable paths. `ensureProvisioned()` is single-flight; `reprovision()`
 * always performs a new attempt.
 */
class FakeTerminalProvisioning(
    private val outcome: ProvisioningResult = ProvisioningResult.Success,
    initial: ProvisioningState = if (outcome is ProvisioningResult.Success) {
        ProvisioningState.Provisioned
    } else {
        ProvisioningState.Unprovisioned
    },
) : TerminalProvisioning {

    private val machine = ProvisioningStateMachine(initial)
    private val mutex = Mutex()

    private var attemptsCount: Int = 0

    /** Number of provisioning attempts performed, for assertions. */
    val attempts: Int get() = attemptsCount

    override val state: StateFlow<ProvisioningState> = machine.state

    override suspend fun ensureProvisioned(): ProvisioningResult = mutex.withLock {
        if (machine.state.value is ProvisioningState.Provisioned) {
            ProvisioningResult.Success
        } else {
            attempt()
        }
    }

    override suspend fun reprovision(): ProvisioningResult = mutex.withLock {
        attempt()
    }

    private fun attempt(): ProvisioningResult {
        attemptsCount += 1
        machine.beginKeyDownload()
        return when (outcome) {
            ProvisioningResult.Success -> {
                machine.beginParameterDownload()
                machine.markProvisioned()
                ProvisioningResult.Success
            }

            is ProvisioningResult.Failure -> {
                machine.markFailed(outcome.reason, outcome.detail)
                outcome
            }
        }
    }
}
