package com.example.pos_agent.domain.card.emv

/** A prompt that has been issued and is waiting for exactly one reply. */
data class PendingPrompt(
    val id: Long,
    val prompt: EmvPrompt,
)

/**
 * Immutable view of a session at one point in time.
 *
 * [pendingPrompt] is non-null only for the short window between issuing an
 * answerable prompt and receiving its reply. The UI can use this snapshot to
 * reattach while the host call is still running.
 */
data class EmvSessionSnapshot(
    val state: EmvState = EmvState.Idle,
    val pendingPrompt: PendingPrompt? = null,
    val cardPresented: Boolean = false,
    val secondTapCount: Int = 0,
    val result: AcquisitionResult? = null,
) {
    val isTerminal: Boolean get() = state.isTerminal
}
