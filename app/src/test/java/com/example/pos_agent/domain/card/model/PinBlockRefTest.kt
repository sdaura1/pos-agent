package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PinBlockRefTest {

    @Test
    fun `accepts a des pin block with a ksn`() {
        val ref = PinBlockRef("0123456789ABCDEF", ksn = "01234567890123456789")

        assertEquals("0123456789ABCDEF", ref.encryptedPinBlock)
    }

    @Test
    fun `accepts an aes pin block without a ksn`() {
        PinBlockRef("0123456789ABCDEF0123456789ABCDEF")
    }

    @Test
    fun `rejects a block with the wrong length`() {
        assertThrows(IllegalArgumentException::class.java) {
            PinBlockRef("0123456789ABCD")
        }
    }

    @Test
    fun `rejects a non-hex block`() {
        assertThrows(IllegalArgumentException::class.java) {
            PinBlockRef("0123456789ABCDGG")
        }
    }

    @Test
    fun `rejects a non-hex ksn`() {
        assertThrows(IllegalArgumentException::class.java) {
            PinBlockRef("0123456789ABCDEF", ksn = "NOT-HEX")
        }
    }

    @Test
    fun `rejects a blank key id`() {
        assertThrows(IllegalArgumentException::class.java) {
            PinBlockRef("0123456789ABCDEF", keyId = "   ")
        }
    }

    @Test
    fun `redacts the block and ksn from toString`() {
        val text = PinBlockRef("0123456789ABCDEF", ksn = "01234567890123456789").toString()

        assertEquals("PinBlockRef(***)", text)
    }
}
