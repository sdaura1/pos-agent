package com.example.pos_agent.domain.terminal.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TerminalTraitsTest {

    @Test
    fun `TerminalTraits should default to an attended POS when created`() {
        // Arrange
        val traits = TerminalTraits()

        // Act
        val pinCapability = traits.pinEntryCapability
        val operator = traits.operator

        // Assert
        assertEquals(PinEntryCapability.UNKNOWN, pinCapability)
        assertEquals(TerminalOperator.CARD_ACCEPTOR, operator)
        assertEquals("01", traits.terminalTypeCode)
        assertNull(traits.maxPinLength)
    }

    @Test
    fun `TerminalTraits should accept the PIN bounds when given 4 and 12`() {
        // Arrange
        val minimum = 4
        val maximum = 12

        // Act
        val low = TerminalTraits(maxPinLength = minimum)
        val high = TerminalTraits(maxPinLength = maximum)

        // Assert
        assertEquals(minimum, low.maxPinLength)
        assertEquals(maximum, high.maxPinLength)
    }

    @Test
    fun `TerminalTraits should reject PIN lengths outside 4 to 12 when given`() {
        // Arrange
        val tooShort = 3
        val tooLong = 13

        // Act
        val shortFailure = runCatching {
            TerminalTraits(maxPinLength = tooShort)
        }.exceptionOrNull()
        val longFailure = runCatching {
            TerminalTraits(maxPinLength = tooLong)
        }.exceptionOrNull()

        // Assert
        assertTrue(shortFailure is IllegalArgumentException)
        assertTrue(longFailure is IllegalArgumentException)
    }

    @Test
    fun `TerminalTraits should reject an invalid terminal type when given`() {
        // Arrange
        val oneDigit = "1"
        val letters = "AB"

        // Act
        val oneDigitFailure = runCatching {
            TerminalTraits(terminalTypeCode = oneDigit)
        }.exceptionOrNull()
        val letterFailure = runCatching {
            TerminalTraits(terminalTypeCode = letters)
        }.exceptionOrNull()

        // Assert
        assertTrue(oneDigitFailure is IllegalArgumentException)
        assertTrue(letterFailure is IllegalArgumentException)
    }
}
