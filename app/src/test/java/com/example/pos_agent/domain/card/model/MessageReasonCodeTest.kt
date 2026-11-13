package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageReasonCodeTest {

    @Test
    fun `MessageReasonCode should expose the reversal constants when read`() {
        // Arrange
        val cancellation = MessageReasonCode.CUSTOMER_CANCELLATION
        val timeout = MessageReasonCode.TIMEOUT

        // Act
        val cancellationValue = cancellation.value
        val timeoutValue = timeout.value

        // Assert
        assertEquals("4000", cancellationValue)
        assertEquals("4021", timeoutValue)
    }

    @Test
    fun `MessageReasonCode should accept a four-digit code when constructed`() {
        // Arrange
        val value = "4021"

        // Act
        val code = MessageReasonCode(value)

        // Assert
        assertEquals(value, code.value)
    }

    @Test
    fun `MessageReasonCode should reject a malformed value when constructed`() {
        // Arrange
        val shortCode = "400"
        val nonNumeric = "40A0"

        // Act
        val shortFailure = runCatching {
            MessageReasonCode(shortCode)
        }.exceptionOrNull()
        val nonNumericFailure = runCatching {
            MessageReasonCode(nonNumeric)
        }.exceptionOrNull()

        // Assert
        assertTrue(shortFailure is IllegalArgumentException)
        assertTrue(nonNumericFailure is IllegalArgumentException)
    }
}
