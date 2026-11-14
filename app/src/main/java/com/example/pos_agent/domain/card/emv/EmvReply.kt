package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.EmvHostResponse
import com.example.pos_agent.domain.card.model.PinBlockRef

/**
 * The app's answer to a pending [EmvPrompt].
 *
 * [Cancel] is legal on every answerable prompt, including
 * [EmvPrompt.RequestOnline], and ends the session in
 * [AcquisitionResult.Cancelled]. [PinCancelled] is different: it is a
 * kernel-level PIN cancel that keeps the session alive so the kernel can
 * request another CVM or finish on its own.
 */
sealed interface EmvReply {
    /** Answer to [EmvPrompt.SelectApplication]. */
    data class Select(val index: Int) : EmvReply

    /** Answer to [EmvPrompt.ConfirmCardInfo]. */
    data object Accept : EmvReply

    /** Answer to [EmvPrompt.ConfirmCardInfo]. */
    data object Decline : EmvReply

    /** Answer to [EmvPrompt.RequestPin]. */
    data class PinEntered(val pinBlockRef: PinBlockRef) : EmvReply

    /** Answer to [EmvPrompt.RequestPin]; the kernel decides what happens next. */
    data object PinCancelled : EmvReply

    /** Answer to [EmvPrompt.RequestPin]; the PIN pad deadline elapsed. */
    data object PinTimeout : EmvReply

    /** Answer to [EmvPrompt.RequestOnline]. */
    data class HostApproved(val hostResponse: EmvHostResponse? = null) : EmvReply

    /** Answer to [EmvPrompt.RequestOnline]. */
    data class HostDeclined(val hostResponse: EmvHostResponse? = null) : EmvReply

    /**
     * Answer to [EmvPrompt.RequestOnline] when the host was not reached.
     *
     * This is not a failure. The caller must mark the transaction `UNKNOWN`
     * and reconcile it by reference.
     */
    data class HostUnknown(val hostResponse: EmvHostResponse? = null) : EmvReply

    /** Ends the session and answers the pending prompt, if any. */
    data object Cancel : EmvReply
}
