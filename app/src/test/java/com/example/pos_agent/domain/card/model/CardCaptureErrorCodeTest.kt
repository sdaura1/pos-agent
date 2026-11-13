package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CardCaptureErrorCodeTest {

    @Test
    fun `all capture codes are non-blank and unique`() {
        val codes = listOf(
            CardCaptureErrorCode.TIMEOUT,
            CardCaptureErrorCode.CANCELLED,
            CardCaptureErrorCode.CARD_REMOVED,
            CardCaptureErrorCode.UNSUPPORTED_CARD,
            CardCaptureErrorCode.PIN_BLOCKED,
            CardCaptureErrorCode.PIN_TIMEOUT,
            CardCaptureErrorCode.TERMINAL_UNAVAILABLE,
            CardCaptureErrorCode.PRINTER_UNAVAILABLE,
            CardCaptureErrorCode.UNRECONCILED,
        )

        assertTrue(codes.all { it.isNotBlank() })
        assertEquals(codes.size, codes.toSet().size)
    }
}
