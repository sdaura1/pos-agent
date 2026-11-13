package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NfcDataTest {

    @Test
    fun `NfcData should accept a payload at the DE124 limit when constructed`() {
        // Arrange
        val payload = "A".repeat(9999)

        // Act
        val data = NfcData(payload)

        // Assert
        assertEquals(9999, data.value.length)
    }

    @Test
    fun `NfcData should reject an empty payload when constructed`() {
        // Arrange
        val empty = ""

        // Act
        val failure = runCatching { NfcData(empty) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `NfcData should reject a payload above the DE124 limit when built`() {
        // Arrange
        val payload = "A".repeat(10_000)

        // Act
        val failure = runCatching { NfcData(payload) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `NfcData should redact its payload when converted to text`() {
        // Arrange
        val data = NfcData("PAYLOAD")

        // Act
        val text = data.toString()

        // Assert
        assertEquals("NfcData(***)", text)
    }
}
