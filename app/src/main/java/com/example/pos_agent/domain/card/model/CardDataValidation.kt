package com.example.pos_agent.domain.card.model

/**
 * Small validation helpers for domain values.
 *
 * They live here rather than in a data-layer codec because the domain must be
 * able to reject malformed card data before any adapter sees it.
 */
internal fun isHex(value: String): Boolean =
    value.isNotEmpty() &&
        value.length % 2 == 0 &&
        value.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }

internal fun isDigits(value: String, length: Int): Boolean =
    value.length == length && value.all(Char::isDigit)

/**
 * True when the value contains a digit run that is both PAN-length and passes
 * the Luhn check. A plain length check produces false positives for AIDs and
 * other hex identifiers with long zero runs.
 */
internal fun containsPanLike(value: String): Boolean =
    PAN_CANDIDATE.findAll(value).any { luhnValid(it.value) }

private val PAN_CANDIDATE = Regex("\\d{13,19}")

private fun luhnValid(number: String): Boolean {
    var sum = 0
    var double = false
    for (index in number.indices.reversed()) {
        var digit = number[index].digitToInt()
        if (double) {
            digit *= 2
            if (digit > 9) digit -= 9
        }
        sum += digit
        double = !double
    }
    return sum % 10 == 0
}
