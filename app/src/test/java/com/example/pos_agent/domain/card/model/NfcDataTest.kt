package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class NfcDataTest {

    @Test
    fun `accepts a payload up to the de124 maximum`() {
        val payload = "A".repeat(9999)

        assertEquals(9999, NfcData(payload).value.length)
    }

    @Test
    fun `rejects an empty payload`() {
        assertThrows(IllegalArgumentException::class.java) { NfcData("") }
    }

    @Test
    fun `rejects a payload above the de124 maximum`() {
        assertThrows(IllegalArgumentException::class.java) { NfcData("A".repeat(10_000)) }
    }

    @Test
    fun `redacts the payload from toString`() {
        assertEquals("NfcData(***)", NfcData("PAYLOAD").toString())
    }
}
