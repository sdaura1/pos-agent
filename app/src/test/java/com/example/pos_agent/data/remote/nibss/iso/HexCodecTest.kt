package com.example.pos_agent.data.remote.nibss.iso

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HexCodecTest {

    @Test
    fun `encode should produce uppercase hex when given bytes`() {
        // Arrange
        val bytes = byteArrayOf(0, -1, 16)

        // Act
        val encoded = HexCodec.encode(bytes)

        // Assert
        assertEquals("00FF10", encoded)
    }

    @Test
    fun `decodeOrNull should accept either case when the input is valid`() {
        // Arrange
        val expected = byteArrayOf(0x0A.toByte(), 0xBC.toByte())

        // Act
        val lower = HexCodec.decodeOrNull("0abc")
        val upper = HexCodec.decodeOrNull("0ABC")

        // Assert
        assertTrue(expected.contentEquals(lower!!))
        assertTrue(expected.contentEquals(upper!!))
    }

    @Test
    fun `decodeOrNull should return null when the input is malformed`() {
        // Arrange
        val oddLength = "ABC"
        val nonHex = "ZZ"

        // Act
        val oddResult = HexCodec.decodeOrNull(oddLength)
        val nonHexResult = HexCodec.decodeOrNull(nonHex)

        // Assert
        assertEquals(null, oddResult)
        assertEquals(null, nonHexResult)
    }

    @Test
    fun `constantTimeEquals should ignore case when the values match`() {
        // Arrange
        val lower = "00ff"
        val upper = "00FF"

        // Act
        val equal = HexCodec.constantTimeEquals(lower, upper)

        // Assert
        assertTrue(equal)
        assertFalse(HexCodec.constantTimeEquals("00ff", "00fe"))
        assertFalse(HexCodec.constantTimeEquals("00ff", "not-hex"))
    }
}
