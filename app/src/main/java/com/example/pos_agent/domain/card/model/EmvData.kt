package com.example.pos_agent.domain.card.model

/**
 * EMV data in TLV hex form, ready for DE55.
 *
 * TLV can contain sensitive tags (for example PAN tag 5A or track-2 tag 57),
 * so the value is treated as opaque and redacted from [toString]. It is never
 * logged or persisted as a readable value.
 */
@JvmInline
value class EmvData(val value: String) {
    init {
        require(isHex(value)) { "EMV data must be an even-length hex string" }
    }

    override fun toString(): String = "EmvData(***)"
}
