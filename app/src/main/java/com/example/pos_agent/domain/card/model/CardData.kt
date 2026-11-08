package com.example.pos_agent.domain.card.model

/**
 * Opaque, already-encrypted/tokenised card data produced by the card reader.
 * The domain must never see or store a full PAN.
 */
@JvmInline
value class CardToken(val value: String) {
    override fun toString(): String = "CardToken(***)"
}

/** Display-only PAN with the middle digits masked, e.g. 5061********1234. */
@JvmInline
value class MaskedPan(val value: String) {
    init {
        require(!FULL_PAN.matches(value)) {
            "Full PAN must never be stored; mask it first"
        }
    }

    private companion object {
        val FULL_PAN = Regex("\\d{13,19}")
    }
}
