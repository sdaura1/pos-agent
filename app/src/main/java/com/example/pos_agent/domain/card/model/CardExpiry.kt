package com.example.pos_agent.domain.card.model

/** Card expiry in YYMM form, as printed on the card and used by DE14. */
@JvmInline
value class CardExpiry(val value: String) {
    init {
        require(isDigits(value, 4)) { "Expiry must be four digits in YYMM form" }
        val month = value.substring(2).toInt()
        require(month in 1..12) { "Expiry month must be between 01 and 12" }
    }
}
