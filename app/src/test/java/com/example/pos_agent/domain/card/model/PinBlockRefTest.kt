package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PinBlockRefTest {

    @Test
    fun `PinBlockRef should accept a DES block with a KSN when constructed`() {
        // Arrange
        val block = "0123456789ABCDEF"
        val ksn = "01234567890123456789"

        // Act
        val ref = PinBlockRef(block, ksn = ksn)

        // Assert
        assertEquals(block, ref.encryptedPinBlock)
    }

    @Test
    fun `PinBlockRef should accept an AES block without a KSN when constructed`() {
        // Arrange
        val block = "0123456789ABCDEF0123456789ABCDEF"

        // Act
        val ref = PinBlockRef(block)

        // Assert
        assertEquals(block, ref.encryptedPinBlock)
    }

    @Test
    fun `PinBlockRef should reject a block with the wrong length when built`() {
        // Arrange
        val shortBlock = "0123456789ABCD"

        // Act
        val failure = runCatching { PinBlockRef(shortBlock) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `PinBlockRef should reject a non-hex block when built`() {
        // Arrange
        val invalidBlock = "0123456789ABCDGG"

        // Act
        val failure = runCatching { PinBlockRef(invalidBlock) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `PinBlockRef should reject a non-hex KSN when built`() {
        // Arrange
        val block = "0123456789ABCDEF"
        val invalidKsn = "NOT-HEX"

        // Act
        val failure = runCatching {
            PinBlockRef(block, ksn = invalidKsn)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `PinBlockRef should reject a blank key id when built`() {
        // Arrange
        val block = "0123456789ABCDEF"
        val blankKeyId = "   "

        // Act
        val failure = runCatching {
            PinBlockRef(block, keyId = blankKeyId)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `PinBlockRef should redact the block and KSN when converted to text`() {
        // Arrange
        val ref = PinBlockRef(
            "0123456789ABCDEF",
            ksn = "01234567890123456789",
        )

        // Act
        val text = ref.toString()

        // Assert
        assertEquals("PinBlockRef(***)", text)
    }
}
