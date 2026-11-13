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
    fun `an already provisioned terminal should return success when checked`() = runBlocking {
        // Arrange
        val provisioning = FakeTerminalProvisioning()
        val useCase = EnsureTerminalProvisionedUseCase(provisioning)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result is Response.Success)
    }

    @Test
    fun `the use case should expose a stable code when the service is missing`() = runBlocking {
        // Arrange
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(
                ProvisioningFailureReason.SERVICE_UNAVAILABLE,
            ),
        )
        val useCase = EnsureTerminalProvisionedUseCase(provisioning)

        // Act
        val result = useCase()

        // Assert
        val failure = result as Response.Failure
        assertEquals(ProvisioningErrorCode.SERVICE_UNAVAILABLE, failure.error.code)
        assertTrue(failure.error is DomainError.Unknown)
    }

    @Test
    fun `the use case should return a conflict when the version mismatches`() = runBlocking {
        // Arrange
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(
                ProvisioningFailureReason.VERSION_MISMATCH,
            ),
        )
        val useCase = EnsureTerminalProvisionedUseCase(provisioning)

        // Act
        val result = useCase()

        // Assert
        val failure = result as Response.Failure
        assertEquals(ProvisioningErrorCode.VERSION_MISMATCH, failure.error.code)
        assertTrue(failure.error is DomainError.Conflict)
    }

    @Test
    fun `the use case should return a network error when key download fails`() = runBlocking {
        // Arrange
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(
                ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
            ),
        )
        val useCase = EnsureTerminalProvisionedUseCase(provisioning)

        // Act
        val result = useCase()

        // Assert
        val failure = result as Response.Failure
        assertEquals(ProvisioningErrorCode.KEY_DOWNLOAD_FAILED, failure.error.code)
        assertTrue(failure.error is DomainError.Network)
    }

    @Test
    fun `the use case should return a code when any failure reason occurs`() = runBlocking {
        for (reason in ProvisioningFailureReason.entries) {
            // Arrange
            val provisioning = FakeTerminalProvisioning(
                outcome = ProvisioningResult.Failure(reason),
            )
            val useCase = EnsureTerminalProvisionedUseCase(provisioning)

            // Act
            val result = useCase()

            // Assert
            val failure = result as Response.Failure
            assertNotNull("Missing code for $reason", failure.error.code)
            assertTrue("Blank code for $reason", failure.error.code!!.isNotBlank())
        }
    }

    @Test
    fun `the use case should carry detail into the message when provided`() = runBlocking {
        // Arrange
        val provisioning = FakeTerminalProvisioning(
            outcome = ProvisioningResult.Failure(
                ProvisioningFailureReason.KEY_DOWNLOAD_FAILED,
                detail = "host rejected TMK",
            ),
        )
        val useCase = EnsureTerminalProvisionedUseCase(provisioning)

        // Act
        val result = useCase()

        // Assert
        assertEquals(
            "host rejected TMK",
            (result as Response.Failure).error.message,
        )
    }
}
