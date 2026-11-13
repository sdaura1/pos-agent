package com.example.pos_agent.domain.terminal.usecase

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.data.device.fake.FakeTerminalProvisioning
import com.example.pos_agent.domain.terminal.model.ProvisioningErrorCode
import com.example.pos_agent.domain.terminal.model.ProvisioningFailureReason
import com.example.pos_agent.domain.terminal.model.ProvisioningResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EnsureTerminalProvisionedUseCaseTest {

    @Test
    fun `provisioned terminal returns success`() = runBlocking {
        val provisioning = FakeTerminalProvisioning()

        val result = EnsureTerminalProvisionedUseCase(provisioning)()

        assertTrue(result is Response.Success)
    }

    @Test
    fun `missing service exposes a stable code`() = runBlocking {
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(ProvisioningFailureReason.SERVICE_UNAVAILABLE),
        )

        val result = EnsureTerminalProvisionedUseCase(provisioning)()

        val failure = result as Response.Failure
        assertEquals(ProvisioningErrorCode.SERVICE_UNAVAILABLE, failure.error.code)
        assertTrue(failure.error is DomainError.Unknown)
    }

    @Test
    fun `version mismatch is a conflict`() = runBlocking {
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(ProvisioningFailureReason.VERSION_MISMATCH),
        )

        val result = EnsureTerminalProvisionedUseCase(provisioning)()

        val failure = result as Response.Failure
        assertEquals(ProvisioningErrorCode.VERSION_MISMATCH, failure.error.code)
        assertTrue(failure.error is DomainError.Conflict)
    }

    @Test
    fun `download failures are network errors`() = runBlocking {
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(ProvisioningFailureReason.KEY_DOWNLOAD_FAILED),
        )

        val result = EnsureTerminalProvisionedUseCase(provisioning)()

        val failure = result as Response.Failure
        assertEquals(ProvisioningErrorCode.KEY_DOWNLOAD_FAILED, failure.error.code)
        assertTrue(failure.error is DomainError.Network)
    }

    @Test
    fun `every failure reason maps to a non-empty code`() = runBlocking {
        for (reason in ProvisioningFailureReason.entries) {
            val provisioning = FakeTerminalProvisioning(
                outcome = ProvisioningResult.Failure(reason),
            )

            val result = EnsureTerminalProvisionedUseCase(provisioning)()

            val failure = result as Response.Failure
            assertNotNull("Missing code for $reason", failure.error.code)
            assertTrue("Blank code for $reason", failure.error.code!!.isNotBlank())
        }
    }

    @Test
    fun `detail is carried into the domain error message`() = runBlocking {
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(
                ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
                detail = "host rejected TMK",
            ),
        )

        val result = EnsureTerminalProvisionedUseCase(provisioning)()

        assertEquals("host rejected TMK", (result as Response.Failure).error.message)
    }
}
