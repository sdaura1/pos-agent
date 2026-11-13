package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class MessageReasonCodeTest {

    @Test
    fun `exposes the reversal codes used by the host`() {
        assertEquals("4000", MessageReasonCode.CUSTOMER_CANCELLATION.value)
        assertEquals("4021", MessageReasonCode.TIMEOUT.value)
    }

    @Test
    fun `accepts any four digit code`() {
        assertEquals("4021", MessageReasonCode("4021").value)
    }

    @Test
    fun `rejects a code that is not four digits`() {
        assertThrows(IllegalArgumentException::class.java) { MessageReasonCode("400") }
        assertThrows(IllegalArgumentException::class.java) { MessageReasonCode("40A0") }
    }
}
