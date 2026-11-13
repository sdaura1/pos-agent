package com.example.pos_agent.domain.terminal.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ProvisioningStateMachineTest {

    @Test
    fun `starts unprovisioned`() {
        val machine = ProvisioningStateMachine()

        assertEquals(ProvisioningState.Unprovisioned, machine.state.value)
    }

    @Test
    fun `happy path reaches provisioned`() {
        val machine = ProvisioningStateMachine()

        machine.beginKeyDownload()
        assertEquals(ProvisioningState.DownloadingKeys, machine.state.value)

        machine.beginParameterDownload()
        assertEquals(ProvisioningState.DownloadingParameters, machine.state.value)

        machine.markProvisioned()
        assertEquals(ProvisioningState.Provisioned, machine.state.value)
    }

    @Test
    fun `provisioned cannot be reached before parameters`() {
        val machine = ProvisioningStateMachine()
        machine.beginKeyDownload()

        assertThrows(IllegalArgumentException::class.java) { machine.markProvisioned() }
        assertEquals(ProvisioningState.DownloadingKeys, machine.state.value)
    }

    @Test
    fun `parameters cannot be downloaded before keys`() {
        val machine = ProvisioningStateMachine()

        assertThrows(IllegalArgumentException::class.java) { machine.beginParameterDownload() }
        assertEquals(ProvisioningState.Unprovisioned, machine.state.value)
    }

    @Test
    fun `failure can be recorded before the first attempt`() {
        val machine = ProvisioningStateMachine()

        machine.markFailed(ProvisioningFailureReason.SERVICE_UNAVAILABLE, "no service")

        assertEquals(
            ProvisioningState.Failed(ProvisioningFailureReason.SERVICE_UNAVAILABLE, "no service"),
            machine.state.value,
        )
    }

    @Test
    fun `failure can be recorded mid-download`() {
        val machine = ProvisioningStateMachine()
        machine.beginKeyDownload()

        machine.markFailed(ProvisioningFailureReason.KEY_DOWNLOAD_FAILED)

        assertEquals(
            ProvisioningState.Failed(ProvisioningFailureReason.KEY_DOWNLOAD_FAILED),
            machine.state.value,
        )
    }

    @Test
    fun `provisioned is not revoked by a failure`() {
        val machine = readyMachine()

        assertThrows(IllegalArgumentException::class.java) {
            machine.markFailed(ProvisioningFailureReason.UNKNOWN)
        }
        assertEquals(ProvisioningState.Provisioned, machine.state.value)
    }

    @Test
    fun `refresh can start again from provisioned`() {
        val machine = readyMachine()

        machine.beginKeyDownload()

        assertEquals(ProvisioningState.DownloadingKeys, machine.state.value)
    }

    @Test
    fun `failed provisioning can be reset`() {
        val machine = ProvisioningStateMachine()
        machine.markFailed(ProvisioningFailureReason.SERVICE_UNAVAILABLE)

        machine.reset()

        assertEquals(ProvisioningState.Unprovisioned, machine.state.value)
    }

    @Test
    fun `reset is rejected when not failed`() {
        val machine = ProvisioningStateMachine()

        assertThrows(IllegalArgumentException::class.java) { machine.reset() }
    }

    private fun readyMachine(): ProvisioningStateMachine = ProvisioningStateMachine().apply {
        beginKeyDownload()
        beginParameterDownload()
        markProvisioned()
    }
}
