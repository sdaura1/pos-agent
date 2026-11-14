package com.example.pos_agent.domain.card.emv

/**
 * What an adapter should do after handing a prompt to an [EmvSessionDriver].
 *
 * Only [Answered] carries a value that must be sent back to the kernel.
 * [Acknowledged] means the prompt was informational, and [Rejected] means the
 * adapter should stop the kernel rather than wait for a reply.
 */
sealed interface EmvPromptDelivery {
    /** Forward [reply] to the vendor SDK. */
    data class Answered(val reply: EmvReply) : EmvPromptDelivery

    /** No vendor response is required. */
    data object Acknowledged : EmvPromptDelivery

    /** The prompt was illegal; stop the kernel and log [reason] in debug builds. */
    data class Rejected(val reason: EmvRejection) : EmvPromptDelivery
}
