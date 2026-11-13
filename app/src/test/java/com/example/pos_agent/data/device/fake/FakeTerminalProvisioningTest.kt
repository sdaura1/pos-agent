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
    fun `starts provisioned by default without performing an attempt`() = runTest {
        val fake = FakeTerminalProvisioning()

        assertEquals(ProvisioningState.Provisioned, fake.state.value)
        assertEquals(ProvisioningResult.Success, fake.ensureProvisioned())
        assertEquals(0, fake.attempts)
    }

    @Test
    fun `unprovisioned terminal reaches provisioned once`() = runTest {
        val fake = FakeTerminalProvisioning(initial = ProvisioningState.Unprovisioned)

        assertEquals(ProvisioningResult.Success, fake.ensureProvisioned())
        assertEquals(ProvisioningState.Provisioned, fake.state.value)
        assertEquals(1, fake.attempts)
    }

    @Test
    fun `ensureProvisioned is idempotent once provisioned`() = runTest {
        val fake = FakeTerminalProvisioning(initial = ProvisioningState.Unprovisioned)

        fake.ensureProvisioned()
        fake.ensureProvisioned()
        fake.ensureProvisioned()

        assertEquals(1, fake.attempts)
    }

    @Test
    fun `refresh always performs a new attempt`() = runTest {
        val fake = FakeTerminalProvisioning(initial = ProvisioningState.Unprovisioned)

        fake.ensureProvisioned()
        fake.reprovision()

        assertEquals(2, fake.attempts)
        assertEquals(ProvisioningState.Provisioned, fake.state.value)
    }

    @Test
    fun `failure leaves the terminal failed`() = runTest {
        val outcome = ProvisioningResult.Failure(
            ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
            detail = "host rejected TMK",
        )
        val fake = FakeTerminalProvisioning(outcome = outcome)

        val result = fake.ensureProvisioned()

        assertEquals(outcome, result)
        assertEquals(
            ProvisioningState.Failed(ProvisioningFailureReason.KEY_DOWNLOAD_FAILED, "host rejected TMK"),
            fake.state.value,
        )
    }

    @Test
    fun `a failed terminal can retry`() = runTest {
        val fake = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(ProvisioningFailureReason.KEY_DOWNLOAD_FAILED),
        )

        fake.ensureProvisioned()
        fake.ensureProvisioned()

        assertEquals(2, fake.attempts)
    }

    @Test
    fun `concurrent callers share a single attempt`() = runTest {
        val fake = FakeTerminalProvisioning(initial = ProvisioningState.Unprovisioned)

        val results = listOf(
            async { fake.ensureProvisioned() },
            async { fake.ensureProvisioned() },
        ).awaitAll()

        assertTrue(results.all { it == ProvisioningResult.Success })
        assertEquals(1, fake.attempts)
    }
}
