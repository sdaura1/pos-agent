package com.example.pos_agent.domain.shared

/**
 * Money in minor units (kobo for NGN).
 *
 * NIP also transmits minor units, so this maps 1:1 at the NIBSS boundary.
 * Never use Double/Float for money.
 */
data class Money(
    val minorUnits: Long,
    val currency: Currency,
) {
    operator fun plus(other: Money): Money {
        requireSameCurrency(other)
        return Money(minorUnits + other.minorUnits, currency)
    }

    operator fun minus(other: Money): Money {
        requireSameCurrency(other)
        return Money(minorUnits - other.minorUnits, currency)
    }

    operator fun times(quantity: Int): Money = Money(minorUnits * quantity, currency)

    operator fun compareTo(other: Money): Int {
        requireSameCurrency(other)
        return minorUnits.compareTo(other.minorUnits)
    }

    fun isZero(): Boolean = minorUnits == 0L

    private fun requireSameCurrency(other: Money) {
        require(currency == other.currency) {
            "Cannot combine ${currency.code} with ${other.currency.code}"
        }
    }

    companion object {
        fun of(minorUnits: Long, currency: Currency = Currency.NGN): Money =
            Money(minorUnits, currency)

        fun zero(currency: Currency = Currency.NGN): Money = Money(0, currency)
    }
}

@JvmInline
value class Currency(val code: String) {
    init {
        require(code.length == 3) { "Expected ISO-4217 code, got '$code'" }
    }

    companion object {
        val NGN = Currency("NGN")
    }
}
