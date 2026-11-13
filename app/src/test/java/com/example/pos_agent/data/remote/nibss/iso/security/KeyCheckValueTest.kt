package com.example.pos_agent.data.remote.nibss.iso.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyCheckValueTest {

    @Test
    fun `single des key check value matches the known vector`() {
        assertEquals("D5D44F", KeyCheckValue.calculate(hex("0123456789ABCDEF")))
    }

    @Test
    fun `two key triple des key check value matches the known vector`() {
        assertEquals("08D7B4", KeyCheckValue.calculate(hex("0123456789ABCDEFFEDCBA9876543210")))
    }

    @Test
    fun `three key triple des key check value matches the known vector`() {
        assertEquals(
            "08D7B4",
            KeyCheckValue.calculate(hex("0123456789ABCDEFFEDCBA98765432100123456789ABCDEF")),
        )
    }

    @Test
    fun `raw key check value is three bytes`() {
        assertEquals(3, KeyCheckValue.calculateBytes(hex("0123456789ABCDEF")).size)
    }

    @Test
    fun `rejects key lengths that are not des or triple des`() {
        for (length in listOf(0, 7, 9, 15, 20, 32)) {
            assertThrows(IllegalArgumentException::class.java) {
                KeyCheckValue.calculate(ByteArray(length))
            }
        }
    }

    @Test
    fun `matches is case insensitive`() {
        assertTrue(KeyCheckValue.matches("d5d44f", "D5D44F"))
        assertTrue(KeyCheckValue.matches("D5D44F", "D5D44F"))
    }

    @Test
    fun `matches rejects different values and malformed hex`() {
        assertFalse(KeyCheckValue.matches("D5D44F", "D5D440"))
        assertFalse(KeyCheckValue.matches("D5D44F", "not-hex"))
        assertFalse(KeyCheckValue.matches("D5D44F", "D5D44"))
    }

    private fun hex(value: String): ByteArray =
        value.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}
