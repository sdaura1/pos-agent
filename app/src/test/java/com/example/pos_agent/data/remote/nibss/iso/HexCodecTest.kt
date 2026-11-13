package com.example.pos_agent.data.remote.nibss.iso

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HexCodecTest {

    @Test
    fun `encodes bytes as uppercase hex`() {
        assertEquals("00FF10", HexCodec.encode(byteArrayOf(0, -1, 16)))
    }

    @Test
    fun `decodes valid hex regardless of case`() {
        assertTrue(byteArrayOf(0x0A.toByte(), 0xBC.toByte()).contentEquals(HexCodec.decodeOrNull("0abc")!!))
        assertTrue(byteArrayOf(0x0A.toByte(), 0xBC.toByte()).contentEquals(HexCodec.decodeOrNull("0ABC")!!))
    }

    @Test
    fun `rejects odd length and non-hex input`() {
        assertEquals(null, HexCodec.decodeOrNull("ABC"))
        assertEquals(null, HexCodec.decodeOrNull("ZZ"))
    }

    @Test
    fun `constant time comparison is case insensitive`() {
        assertTrue(HexCodec.constantTimeEquals("00ff", "00FF"))
        assertFalse(HexCodec.constantTimeEquals("00ff", "00fe"))
        assertFalse(HexCodec.constantTimeEquals("00ff", "not-hex"))
    }
}
