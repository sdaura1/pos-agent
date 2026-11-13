package com.example.pos_agent.domain.card.model

/**
 * Contactless/NFC payload as received from the terminal (DE124).
 *
 * The payload can contain card data, so it is redacted from [toString] and
 * never persisted or logged.
 */
@JvmInline
value class NfcData(val value: String) {
    init {
        require(value.isNotEmpty()) { "NFC data cannot be empty" }
        require(value.length <= 9999) { "NFC data exceeds the DE124 maximum" }
    }

    override fun toString(): String = "NfcData(***)"
}
