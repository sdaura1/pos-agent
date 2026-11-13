package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CardExpiryTest {

    @Test
    fun `CardExpiry should accept a valid YYMM when constructed`() {
        // Arrange
        val december = "2912"
        val january = "2701"

        // Act
        val first = CardExpiry(december)
        val second = CardExpiry(january)

        // Assert
        assertEquals(december, first.value)
        assertEquals(january, second.value)
    }

    @Test
    fun `CardExpiry should reject a month outside 01 to 12 when constructed`() {
        // Arrange
        val monthThirteen = "2913"
        val monthZero = "2900"

        // Act
        val firstFailure = runCatching { CardExpiry(monthThirteen) }.exceptionOrNull()
        val secondFailure = runCatching { CardExpiry(monthZero) }.exceptionOrNull()

        // Assert
        assertTrue(firstFailure is IllegalArgumentException)
        assertTrue(secondFailure is IllegalArgumentException)
    }

    @Test
    fun `CardExpiry should reject a value that is not four digits when built`() {
        // Arrange
        val shortValue = "291"
        val letters = "29AA"

        // Act
        val shortFailure = runCatching { CardExpiry(shortValue) }.exceptionOrNull()
        val letterFailure = runCatching { CardExpiry(letters) }.exceptionOrNull()

        // Assert
        assertTrue(shortFailure is IllegalArgumentException)
        assertTrue(letterFailure is IllegalArgumentException)
    }
}
