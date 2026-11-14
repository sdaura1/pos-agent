package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardCaptureErrorCode

/**
 * Pure state machine for one EMV acquisition session.
 *
 * The adapter feeds kernel prompts in and returns the app's replies. The
 * machine owns the ordering, reply-once, cancellation, timeout, second-tap and
 * card-removal rules. It has no timer, no network, no persistence and no
 * Android or vendor type.
 *
 * Instances are single-use and not thread-safe. [EmvSessionDriver] serialises
 * calls from adapter callbacks and the UI.
 */
class EmvSession(
    private val policy: EmvSessionPolicy = EmvSessionPolicy(),
) {
    var snapshot: EmvSessionSnapshot = EmvSessionSnapshot()
        private set

    private var started = false
    private var nextPromptId = 0L
    private var lastAnsweredPromptId: Long? = null

    /** Starts the card wait. Idle is the only state where this is legal. */
    fun start(): EmvTransition {
        if (snapshot.isTerminal) return reject(EmvRejection.SessionFinished)
        if (started) return reject(EmvRejection.SessionAlreadyStarted)
        started = true
        return accept(snapshot.copy(state = EmvState.WaitingForCard))
    }

    /** Reports a kernel prompt or an informational kernel event. */
    fun onPrompt(prompt: EmvPrompt): EmvTransition {
        if (snapshot.isTerminal) return reject(EmvRejection.SessionFinished)
        if (!started) return reject(EmvRejection.SessionNotStarted)
        snapshot.pendingPrompt?.let { pending ->
            return reject(EmvRejection.PromptAlreadyPending(pending, prompt))
        }

        return when (prompt) {
            is EmvPrompt.CardPresented -> onCardPresented(prompt)
            is EmvPrompt.KernelSelected -> accept(snapshot)
            is EmvPrompt.SecondTap -> onSecondTap(prompt)
            is EmvPrompt.Failure -> resolve(
                state = EmvState.Failed,
                result = AcquisitionResult.Failed(prompt.code, prompt.detail),
            )
            is EmvPrompt.Finished -> onFinished(prompt)
            is EmvPrompt.SelectApplication ->
                awaitReply(EmvState.ApplicationSelection, prompt)
            is EmvPrompt.ConfirmCardInfo ->
                awaitReply(EmvState.CardholderConfirmation, prompt)
            is EmvPrompt.RequestPin -> awaitReply(EmvState.PinEntry, prompt)
            is EmvPrompt.RequestOnline -> awaitReply(EmvState.Online, prompt)
        }
    }

    /**
     * Answers the prompt with [promptId].
     *
     * A stale id is rejected rather than applied to the next prompt, so a
     * double tap on a confirmation button cannot answer two prompts.
     */
    fun reply(promptId: Long, reply: EmvReply): EmvTransition {
        if (snapshot.isTerminal) return reject(EmvRejection.SessionFinished)
        if (!started) return reject(EmvRejection.SessionNotStarted)

        val pending = snapshot.pendingPrompt ?: return reject(
            if (lastAnsweredPromptId == promptId) {
                EmvRejection.StalePromptReply(promptId)
            } else {
                EmvRejection.NoPendingPrompt
            }
        )
        if (pending.id != promptId) return reject(EmvRejection.StalePromptReply(promptId))

        val rejection = validateReply(pending.prompt, reply)
        if (rejection != null) return reject(rejection)

        lastAnsweredPromptId = pending.id
        val answered = snapshot.copy(pendingPrompt = null)
        return when (reply) {
            EmvReply.Cancel -> resolve(
                state = EmvState.Cancelled,
                result = AcquisitionResult.Cancelled,
                base = answered,
                answeredPrompt = pending,
                reply = EmvReply.Cancel,
            )

            EmvReply.PinTimeout -> resolve(
                state = EmvState.TimedOut,
                result = AcquisitionResult.TimedOut(AcquisitionTimeout.PIN),
                base = answered,
                answeredPrompt = pending,
                reply = EmvReply.PinTimeout,
            )

            else -> accept(answered)
        }
    }

    /**
     * Cancels the session, answering the pending prompt with [EmvReply.Cancel].
     *
     * Cancelling an idle session is allowed so no caller has to special-case a
     * session that never started.
     */
    fun cancel(): EmvTransition {
        if (snapshot.isTerminal) return reject(EmvRejection.SessionFinished)
        val pending = snapshot.pendingPrompt
        if (pending != null) lastAnsweredPromptId = pending.id
        return resolve(
            state = EmvState.Cancelled,
            result = AcquisitionResult.Cancelled,
            answeredPrompt = pending,
            reply = if (pending != null) EmvReply.Cancel else null,
        )
    }

    /**
     * Fails the session because the card left the reader.
     *
     * Any pending prompt is answered with [EmvReply.Cancel] and no capture is
     * produced.
     */
    fun onCardRemoved(): EmvTransition {
        if (snapshot.isTerminal) return reject(EmvRejection.SessionFinished)
        if (!started) return reject(EmvRejection.SessionNotStarted)
        val pending = snapshot.pendingPrompt
        if (pending != null) lastAnsweredPromptId = pending.id
        return resolve(
            state = EmvState.Failed,
            result = AcquisitionResult.Failed(CardCaptureErrorCode.CARD_REMOVED),
            answeredPrompt = pending,
            reply = if (pending != null) EmvReply.Cancel else null,
        )
    }

    /**
     * Reports an expired card wait or second-tap deadline.
     *
     * PIN and online deadlines are not reported here: the PIN pad answers with
     * [EmvReply.PinTimeout] and the host call answers with
     * [EmvReply.HostUnknown].
     */
    fun onTimeout(): EmvTransition {
        if (snapshot.isTerminal) return reject(EmvRejection.SessionFinished)
        if (!started) return reject(EmvRejection.SessionNotStarted)

        val timeout = when {
            snapshot.state == EmvState.WaitingForCard && !snapshot.cardPresented ->
                AcquisitionTimeout.CARD_WAIT

            snapshot.state == EmvState.SecondTap && !snapshot.cardPresented ->
                AcquisitionTimeout.SECOND_TAP

            else -> return reject(EmvRejection.UnexpectedTimeout(snapshot.state))
        }

        return resolve(
            state = EmvState.TimedOut,
            result = AcquisitionResult.TimedOut(timeout),
        )
    }

    private fun onCardPresented(prompt: EmvPrompt.CardPresented): EmvTransition {
        val canPresent = !snapshot.cardPresented &&
            (snapshot.state == EmvState.WaitingForCard ||
                snapshot.state == EmvState.SecondTap)
        if (!canPresent) return reject(EmvRejection.UnexpectedPrompt(snapshot.state, prompt))
        return accept(snapshot.copy(cardPresented = true))
    }

    private fun onSecondTap(prompt: EmvPrompt.SecondTap): EmvTransition {
        if (!snapshot.cardPresented) return reject(EmvRejection.CardNotPresented(prompt))
        if (snapshot.secondTapCount >= policy.maxSecondTaps) {
            return resolve(
                state = EmvState.Failed,
                result = AcquisitionResult.Failed(
                    code = CardCaptureErrorCode.CARD_REMOVED,
                    detail = "second tap limit reached",
                ),
            )
        }

        return accept(
            snapshot.copy(
                state = EmvState.SecondTap,
                cardPresented = false,
                secondTapCount = snapshot.secondTapCount + 1,
            ),
        )
    }

    private fun onFinished(prompt: EmvPrompt.Finished): EmvTransition {
        if (!snapshot.cardPresented) return reject(EmvRejection.CardNotPresented(prompt))
        return when (prompt.decision) {
            EmvDecision.APPROVED -> resolve(
                state = EmvState.Completed,
                result = AcquisitionResult.Approved(prompt.capture),
            )

            EmvDecision.DECLINED -> resolve(
                state = EmvState.Declined,
                result = AcquisitionResult.Declined(prompt.capture),
            )
        }
    }

    private fun awaitReply(state: EmvState, prompt: EmvPrompt): EmvTransition {
        if (!snapshot.cardPresented) return reject(EmvRejection.CardNotPresented(prompt))
        val pending = PendingPrompt(id = ++nextPromptId, prompt = prompt)
        snapshot = snapshot.copy(state = state, pendingPrompt = pending)
        return EmvTransition.AwaitingReply(snapshot, pending)
    }

    private fun validateReply(prompt: EmvPrompt, reply: EmvReply): EmvRejection? =
        when (prompt) {
            is EmvPrompt.SelectApplication -> when (reply) {
                EmvReply.Cancel -> null
                is EmvReply.Select -> if (reply.index in prompt.applications.indices) {
                    null
                } else {
                    EmvRejection.InvalidSelection(reply.index, prompt.applications.size)
                }

                else -> EmvRejection.DisallowedReply(prompt, reply)
            }

            is EmvPrompt.ConfirmCardInfo -> if (
                reply == EmvReply.Accept ||
                reply == EmvReply.Decline ||
                reply == EmvReply.Cancel
            ) {
                null
            } else {
                EmvRejection.DisallowedReply(prompt, reply)
            }

            is EmvPrompt.RequestPin -> if (
                reply is EmvReply.PinEntered ||
                reply == EmvReply.PinCancelled ||
                reply == EmvReply.PinTimeout ||
                reply == EmvReply.Cancel
            ) {
                null
            } else {
                EmvRejection.DisallowedReply(prompt, reply)
            }

            is EmvPrompt.RequestOnline -> if (
                reply is EmvReply.HostApproved ||
                reply is EmvReply.HostDeclined ||
                reply is EmvReply.HostUnknown ||
                reply == EmvReply.Cancel
            ) {
                null
            } else {
                EmvRejection.DisallowedReply(prompt, reply)
            }

            else -> EmvRejection.DisallowedReply(prompt, reply)
        }

    private fun accept(updated: EmvSessionSnapshot): EmvTransition.Accepted {
        snapshot = updated.copy(result = null)
        return EmvTransition.Accepted(snapshot)
    }

    private fun resolve(
        state: EmvState,
        result: AcquisitionResult,
        base: EmvSessionSnapshot = snapshot,
        answeredPrompt: PendingPrompt? = null,
        reply: EmvReply? = null,
    ): EmvTransition.Resolved {
        snapshot = base.copy(state = state, pendingPrompt = null, result = result)
        return EmvTransition.Resolved(
            snapshot = snapshot,
            result = result,
            answeredPrompt = answeredPrompt,
            reply = reply,
        )
    }

    private fun reject(reason: EmvRejection): EmvTransition.Rejected =
        EmvTransition.Rejected(snapshot, reason)
}
