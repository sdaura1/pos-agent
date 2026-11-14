package com.example.pos_agent.domain.card.emv

/**
 * Result of applying one event to an [EmvSession].
 *
 * A rejection leaves the session exactly as it was. A resolution is emitted
 * once per session; later events return [Rejected] with
 * [EmvRejection.SessionFinished].
 */
sealed interface EmvTransition {
    val snapshot: EmvSessionSnapshot

    /** An informational prompt or a non-terminal reply was accepted. */
    data class Accepted(
        override val snapshot: EmvSessionSnapshot,
    ) : EmvTransition

    /** An answerable prompt is waiting for [PendingPrompt.id] to be answered. */
    data class AwaitingReply(
        override val snapshot: EmvSessionSnapshot,
        val prompt: PendingPrompt,
    ) : EmvTransition

    /**
     * The session reached a terminal state.
     *
     * When [answeredPrompt] is non-null the adapter must forward [reply] to the
     * kernel, even though the session has already ended. That is how
     * cancellation and card removal avoid leaving a kernel callback dangling.
     */
    data class Resolved(
        override val snapshot: EmvSessionSnapshot,
        val result: AcquisitionResult,
        val answeredPrompt: PendingPrompt? = null,
        val reply: EmvReply? = null,
    ) : EmvTransition

    /** The event was illegal; the session did not change. */
    data class Rejected(
        override val snapshot: EmvSessionSnapshot,
        val reason: EmvRejection,
    ) : EmvTransition
}
