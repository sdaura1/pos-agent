package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CardExpiryTest {

    @Test
    fun `accepts yymm with a valid month`() {
        assertEquals("2912", CardExpiry("2912").value)
        assertEquals("2701", CardExpiry("2701").value)
    }

    @Test
    fun `rejects a month outside 01 to 12`() {
        assertThrows(IllegalArgumentException::class.java) { CardExpiry("2913") }
        assertThrows(IllegalArgumentException::class.java) { CardExpiry("2900") }
    }

    @Test
    fun `rejects a value that is not four digits`() {
        assertThrows(IllegalArgumentException::class.java) { CardExpiry("291") }
        assertThrows(IllegalArgumentException::class.java) { CardExpiry("29AA") }
    }
}
