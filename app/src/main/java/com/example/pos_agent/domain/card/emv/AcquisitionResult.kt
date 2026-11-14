package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardCapture

/**
 * The single terminal answer produced by one acquisition session.
 *
 * [Approved] and [Declined] both carry the capture because a declined
 * transaction is still persisted. Cancellation, timeout and failure never
 * carry a capture.
 */
sealed interface AcquisitionResult {
    /** The kernel approved and produced a capture. */
    data class Approved(val capture: CardCapture) : AcquisitionResult

    /** The kernel declined, but card data was still read. */
    data class Declined(val capture: CardCapture) : AcquisitionResult

    /** The app or cardholder cancelled the session. */
    data object Cancelled : AcquisitionResult

    /** The card wait, PIN pad or second-tap wait expired. */
    data class TimedOut(val timeout: AcquisitionTimeout) : AcquisitionResult

    /**
     * The session failed before a capture was produced.
     *
     * [code] is a stable [com.example.pos_agent.domain.card.model.CardCaptureErrorCode]
     * value, or another adapter-mapped code. [detail] is for diagnostics only
     * and must never be shown as user-facing text.
     */
    data class Failed(
        val code: String,
        val detail: String? = null,
    ) : AcquisitionResult
}
