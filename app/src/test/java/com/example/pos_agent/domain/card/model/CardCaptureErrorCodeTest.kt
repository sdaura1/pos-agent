package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CardCaptureErrorCodeTest {

    @Test
    fun `capture codes should be non-blank and unique when listed`() {
        // Arrange
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

        // Act
        val allNonBlank = codes.all { it.isNotBlank() }
        val uniqueCount = codes.toSet().size

        // Assert
        assertTrue(allNonBlank)
        assertEquals(codes.size, uniqueCount)
    }
}
