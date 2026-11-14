package com.example.pos_agent.domain.card.emv

/** Configuration that affects which transitions are legal. */
data class EmvSessionPolicy(
    /** How many contactless re-taps the kernel may request before the session fails. */
    val maxSecondTaps: Int = 2,
) {
    init {
        require(maxSecondTaps >= 0) { "Second-tap limit cannot be negative" }
    }
}
