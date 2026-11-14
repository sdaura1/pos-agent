package com.example.pos_agent.domain.card.emv

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Deadlines the driver schedules.
 *
 * PIN entry is not here: the PIN pad owns its own timeout and reports it as
 * [EmvReply.PinTimeout]. Online authorisation is not here either: the host call
 * has its own deadline and reports [EmvReply.HostUnknown].
 */
data class EmvTimeouts(
    val cardWait: Duration = 60.seconds,
    val secondTap: Duration = 30.seconds,
) {
    init {
        require(cardWait > Duration.ZERO) { "Card wait timeout must be positive" }
        require(secondTap > Duration.ZERO) { "Second tap timeout must be positive" }
    }
}
