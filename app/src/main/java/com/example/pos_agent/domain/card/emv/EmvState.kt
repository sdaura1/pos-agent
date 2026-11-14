package com.example.pos_agent.domain.card.emv

/**
 * Position of one EMV acquisition session.
 *
 * A state is a dialogue phase, not a kernel thread. After a prompt has been
 * answered the session stays in the phase with `pendingPrompt == null` until
 * the kernel produces its next callback. That is why the table has no separate
 * "processing" state.
 */
sealed interface EmvState {
    /** No session has started. */
    data object Idle : EmvState

    /** The terminal is waiting for a card, or the card is being read. */
    data object WaitingForCard : EmvState

    /** The kernel matched several applications and needs one selected. */
    data object ApplicationSelection : EmvState

    /** The kernel asked the cardholder to confirm amount or CVM data. */
    data object CardholderConfirmation : EmvState

    /** The kernel asked for an online or offline PIN. */
    data object PinEntry : EmvState

    /** The kernel asked for the host authorisation result. */
    data object Online : EmvState

    /** A contactless card must be presented again. */
    data object SecondTap : EmvState

    /** The kernel finished and produced a capture. */
    data object Completed : EmvState

    /** The kernel declined the transaction. */
    data object Declined : EmvState

    /** The cardholder or the app cancelled the session. */
    data object Cancelled : EmvState

    /** The card wait or the PIN pad timed out. */
    data object TimedOut : EmvState

    /** The session failed, or the card was removed. */
    data object Failed : EmvState
}

/** True when the session has produced its single result and cannot transition again. */
val EmvState.isTerminal: Boolean
    get() = when (this) {
        EmvState.Completed,
        EmvState.Declined,
        EmvState.Cancelled,
        EmvState.TimedOut,
        EmvState.Failed,
        -> true

        else -> false
    }
