package com.example.pos_agent.domain.card.model

/**
 * Stable error codes for card capture failures, consumed by the UI and by the
 * adapter's vendor-code mapping.
 */
object CardCaptureErrorCode {
    const val TIMEOUT = "CARD_CAPTURE_TIMEOUT"
    const val CANCELLED = "CARD_CAPTURE_CANCELLED"
    const val CARD_REMOVED = "CARD_CAPTURE_CARD_REMOVED"
    const val UNSUPPORTED_CARD = "CARD_CAPTURE_UNSUPPORTED_CARD"
    const val PIN_BLOCKED = "CARD_CAPTURE_PIN_BLOCKED"
    const val PIN_TIMEOUT = "CARD_CAPTURE_PIN_TIMEOUT"
    const val TERMINAL_UNAVAILABLE = "CARD_CAPTURE_TERMINAL_UNAVAILABLE"
    const val PRINTER_UNAVAILABLE = "CARD_CAPTURE_PRINTER_UNAVAILABLE"
    const val UNRECONCILED = "CARD_CAPTURE_UNRECONCILED"
}
