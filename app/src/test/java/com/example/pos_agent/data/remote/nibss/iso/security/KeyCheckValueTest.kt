package com.example.pos_agent.data.remote.nibss.iso.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyCheckValueTest {

    @Test
    fun `calculate should match the single DES vector when given an 8-byte key`() {
        // Arrange
        val key = hex("0123456789ABCDEF")

        // Act
        val kcv = KeyCheckValue.calculate(key)

        // Assert
        assertEquals("D5D44F", kcv)
    }

    @Test
    fun `calculate should match the two-key 3DES vector when given a 16-byte key`() {
        // Arrange
        val key = hex("0123456789ABCDEFFEDCBA9876543210")

        // Act
        val kcv = KeyCheckValue.calculate(key)

        // Assert
        assertEquals("08D7B4", kcv)
    }

    @Test
    fun `calculate should match the three-key 3DES vector when given a 24-byte key`() {
        // Arrange
        val key = hex("0123456789ABCDEFFEDCBA98765432100123456789ABCDEF")

        // Act
        val kcv = KeyCheckValue.calculate(key)

        // Assert
        assertEquals("08D7B4", kcv)
    }

    @Test
    fun `calculateBytes should return three bytes when given a DES key`() {
        // Arrange
        val key = hex("0123456789ABCDEF")

        // Act
        val kcv = KeyCheckValue.calculateBytes(key)

        // Assert
        assertEquals(3, kcv.size)
    }

    @Test
    fun `calculate should reject unsupported keys when lengths are invalid`() {
        // Arrange
        val invalidLengths = listOf(0, 7, 9, 15, 20, 32)

        // Act
        val failures = invalidLengths.map { length ->
            runCatching { KeyCheckValue.calculate(ByteArray(length)) }.exceptionOrNull()
        }

        // Assert
        assertTrue(failures.all { it is IllegalArgumentException })
    }

    @Test
    fun `matches should ignore case when the values match`() {
        // Arrange
        val expected = "d5d44f"
        val actual = "D5D44F"

        // Act
        val matches = KeyCheckValue.matches(expected, actual)

        // Assert
        assertTrue(matches)
        assertTrue(KeyCheckValue.matches("D5D44F", "D5D44F"))
    }

    @Test
    fun `matches should return false when values differ or hex is malformed`() {
        // Arrange
        val expected = "D5D44F"

        // Act
        val different = KeyCheckValue.matches(expected, "D5D440")
        val malformed = KeyCheckValue.matches(expected, "not-hex")
        val shortValue = KeyCheckValue.matches(expected, "D5D44")

        // Assert
        assertFalse(different)
        assertFalse(malformed)
        assertFalse(shortValue)
    }

    private fun hex(value: String): ByteArray =
        value.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}
