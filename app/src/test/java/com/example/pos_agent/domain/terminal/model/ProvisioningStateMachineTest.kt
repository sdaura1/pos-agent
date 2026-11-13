package com.example.pos_agent.domain.terminal.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProvisioningStateMachineTest {

    @Test
    fun `a new machine should start unprovisioned when created`() {
        // Arrange
        val machine = ProvisioningStateMachine()

        // Act
        val state = machine.state.value

        // Assert
        assertEquals(ProvisioningState.Unprovisioned, state)
    }

    @Test
    fun `the machine should reach provisioned when keys and parameters load`() {
        // Arrange
        val machine = ProvisioningStateMachine()

        // Act
        machine.beginKeyDownload()
        val afterKeys = machine.state.value
        machine.beginParameterDownload()
        val afterParameters = machine.state.value
        machine.markProvisioned()

        // Assert
        assertEquals(ProvisioningState.DownloadingKeys, afterKeys)
        assertEquals(ProvisioningState.DownloadingParameters, afterParameters)
        assertEquals(ProvisioningState.Provisioned, machine.state.value)
    }

    @Test
    fun `the machine should reject provisioned when parameters are missing`() {
        // Arrange
        val machine = ProvisioningStateMachine()
        machine.beginKeyDownload()

        // Act
        val failure = runCatching { machine.markProvisioned() }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
        assertEquals(ProvisioningState.DownloadingKeys, machine.state.value)
    }

    @Test
    fun `the machine should reject parameters when keys are missing`() {
        // Arrange
        val machine = ProvisioningStateMachine()

        // Act
        val failure = runCatching { machine.beginParameterDownload() }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
        assertEquals(ProvisioningState.Unprovisioned, machine.state.value)
    }

    @Test
    fun `the machine should record a failure when no attempt has run`() {
        // Arrange
        val machine = ProvisioningStateMachine()

        // Act
        machine.markFailed(ProvisioningFailureReason.SERVICE_UNAVAILABLE, "no service")

        // Assert
        assertEquals(
            ProvisioningState.Failed(
                ProvisioningFailureReason.SERVICE_UNAVAILABLE,
                "no service",
            ),
            machine.state.value,
        )
    }

    @Test
    fun `the machine should record a failure when key download is running`() {
        // Arrange
        val machine = ProvisioningStateMachine()
        machine.beginKeyDownload()

        // Act
        machine.markFailed(ProvisioningFailureReason.KEY_DOWNLOAD_FAILED)

        // Assert
        assertEquals(
            ProvisioningState.Failed(ProvisioningFailureReason.KEY_DOWNLOAD_FAILED),
            machine.state.value,
        )
    }

    @Test
    fun `the machine should stay provisioned when a failure is recorded`() {
        // Arrange
        val machine = provisionedMachine()

        // Act
        val failure = runCatching {
            machine.markFailed(ProvisioningFailureReason.UNKNOWN)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
        assertEquals(ProvisioningState.Provisioned, machine.state.value)
    }

    @Test
    fun `the machine should restart key download when already provisioned`() {
        // Arrange
        val machine = provisionedMachine()

        // Act
        machine.beginKeyDownload()

        // Assert
        assertEquals(ProvisioningState.DownloadingKeys, machine.state.value)
    }

    @Test
    fun `the machine should reset when provisioning failed`() {
        // Arrange
        val machine = ProvisioningStateMachine()
        machine.markFailed(ProvisioningFailureReason.SERVICE_UNAVAILABLE)

        // Act
        machine.reset()

        // Assert
        assertEquals(ProvisioningState.Unprovisioned, machine.state.value)
    }

    @Test
    fun `the machine should reject reset when not failed`() {
        // Arrange
        val machine = ProvisioningStateMachine()

        // Act
        val failure = runCatching { machine.reset() }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    private fun provisionedMachine(): ProvisioningStateMachine =
        ProvisioningStateMachine().apply {
            beginKeyDownload()
            beginParameterDownload()
            markProvisioned()
        }
}
