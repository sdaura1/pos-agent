package com.example.pos_agent.data.device.fake

import com.example.pos_agent.domain.terminal.model.ProvisioningFailureReason
import com.example.pos_agent.domain.terminal.model.ProvisioningResult
import com.example.pos_agent.domain.terminal.model.ProvisioningState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeTerminalProvisioningTest {

    @Test
    fun `a default fake should start provisioned when created`() = runTest {
        // Arrange
        val fake = FakeTerminalProvisioning()

        // Act
        val result = fake.ensureProvisioned()

        // Assert
        assertEquals(ProvisioningState.Provisioned, fake.state.value)
        assertEquals(ProvisioningResult.Success, result)
        assertEquals(0, fake.attempts)
    }

    @Test
    fun `an unprovisioned fake should reach provisioned when asked`() = runTest {
        // Arrange
        val fake = FakeTerminalProvisioning(
            initial = ProvisioningState.Unprovisioned,
        )

        // Act
        val result = fake.ensureProvisioned()

        // Assert
        assertEquals(ProvisioningResult.Success, result)
        assertEquals(ProvisioningState.Provisioned, fake.state.value)
        assertEquals(1, fake.attempts)
    }

    @Test
    fun `ensureProvisioned should be idempotent when already provisioned`() = runTest {
        // Arrange
        val fake = FakeTerminalProvisioning(
            initial = ProvisioningState.Unprovisioned,
        )

        // Act
        fake.ensureProvisioned()
        fake.ensureProvisioned()
        fake.ensureProvisioned()

        // Assert
        assertEquals(1, fake.attempts)
    }

    @Test
    fun `reprovision should start a new attempt when already provisioned`() = runTest {
        // Arrange
        val fake = FakeTerminalProvisioning(
            initial = ProvisioningState.Unprovisioned,
        )
        fake.ensureProvisioned()

        // Act
        fake.reprovision()

        // Assert
        assertEquals(2, fake.attempts)
        assertEquals(ProvisioningState.Provisioned, fake.state.value)
    }

    @Test
    fun `a failed attempt should leave the terminal failed when rejected`() = runTest {
        // Arrange
        val failure = ProvisioningResult.Failure(
            ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
            detail = "host rejected TMK",
        )
        val fake = FakeTerminalProvisioning(outcome = failure)

        // Act
        val result = fake.ensureProvisioned()

        // Assert
        assertEquals(failure, result)
        assertEquals(
            ProvisioningState.Failed(
                ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
                "host rejected TMK",
            ),
            fake.state.value,
        )
    }

    @Test
    fun `a failed terminal should retry when ensureProvisioned runs again`() = runTest {
        // Arrange
        val fake = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(
                ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
            ),
        )
        fake.ensureProvisioned()

        // Act
        fake.ensureProvisioned()

        // Assert
        assertEquals(2, fake.attempts)
    }

    @Test
    fun `concurrent callers should share one attempt when they call together`() = runTest {
        // Arrange
        val fake = FakeTerminalProvisioning(
            initial = ProvisioningState.Unprovisioned,
        )

        // Act
        val results = listOf(
            async { fake.ensureProvisioned() },
            async { fake.ensureProvisioned() },
        ).awaitAll()

        // Assert
        assertTrue(results.all { it == ProvisioningResult.Success })
        assertEquals(1, fake.attempts)
    }
}
