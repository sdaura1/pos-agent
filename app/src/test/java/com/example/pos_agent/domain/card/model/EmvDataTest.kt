package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmvDataTest {

    @Test
    fun `EmvData should accept upper and lower case hex when constructed`() {
        // Arrange
        val upper = "9F2608AABB"
        val lower = "9f2608aabb"

        // Act
        val first = EmvData(upper)
        val second = EmvData(lower)

        // Assert
        assertEquals(upper, first.value)
        assertEquals(lower, second.value)
    }

    @Test
    fun `EmvData should reject an odd number of digits when constructed`() {
        // Arrange
        val oddHex = "9F2608AAB"

        // Act
        val failure = runCatching { EmvData(oddHex) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `EmvData should reject non-hex characters when constructed`() {
        // Arrange
        val invalid = "9F26GG"

        // Act
        val failure = runCatching { EmvData(invalid) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `EmvData should reject an empty value when constructed`() {
        // Arrange
        val empty = ""

        // Act
        val failure = runCatching { EmvData(empty) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `EmvData should redact its value when converted to text`() {
        // Arrange
        val data = EmvData("9F2608AABB")

        // Act
        val text = data.toString()

        // Assert
        assertEquals("EmvData(***)", text)
    }
}
