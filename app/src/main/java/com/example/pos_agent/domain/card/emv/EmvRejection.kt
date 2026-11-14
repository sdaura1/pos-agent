package com.example.pos_agent.domain.card.emv

/**
 * Why an event was rejected by [EmvSession].
 *
 * Rejections are values, not exceptions, so the adapter can log them in debug
 * builds and the contract tests can assert on them. The domain never writes a
 * log line itself.
 */
sealed interface EmvRejection {
    /** A prompt or reply arrived before [EmvSession.start]. */
    data object SessionNotStarted : EmvRejection

    /** [EmvSession.start] was called twice on the same session. */
    data object SessionAlreadyStarted : EmvRejection

    /** The session already produced its single result. */
    data object SessionFinished : EmvRejection

    /** A new prompt arrived while an earlier one was still unanswered. */
    data class PromptAlreadyPending(
        val pending: PendingPrompt,
        val prompt: EmvPrompt,
    ) : EmvRejection

    /** The prompt is not legal in the current phase. */
    data class UnexpectedPrompt(
        val state: EmvState,
        val prompt: EmvPrompt,
    ) : EmvRejection

    /** A dialogue prompt arrived before [EmvPrompt.CardPresented]. */
    data class CardNotPresented(val prompt: EmvPrompt) : EmvRejection

    /** A timeout arrived outside the card wait or second-tap wait. */
    data class UnexpectedTimeout(val state: EmvState) : EmvRejection

    /** A reply arrived when no prompt was pending. */
    data object NoPendingPrompt : EmvRejection

    /** A reply targeted an earlier prompt than the one currently pending. */
    data class StalePromptReply(val promptId: Long) : EmvRejection

    /** The reply type is not allowed for the pending prompt. */
    data class DisallowedReply(
        val prompt: EmvPrompt,
        val reply: EmvReply,
    ) : EmvRejection

    /** A selection index fell outside the application list. */
    data class InvalidSelection(
        val index: Int,
        val available: Int,
    ) : EmvRejection

    /** The app's responder threw instead of answering the prompt. */
    data class ResponderFailed(val detail: String? = null) : EmvRejection
}
