package com.example.pos_agent.domain.card.model

/** Internal identity of a stored card transaction. */
@JvmInline
value class TransactionId(val value: String)

/**
 * Idempotency key sent to NIBSS. Retries must reuse the same reference so the
 * switch can deduplicate.
 */
@JvmInline
value class TransactionReference(val value: String) {
    init {
        require(value.isNotBlank()) { "Transaction reference is required" }
    }
}

/** System Trace Audit Number: 6 digits, unique per terminal per day. */
@JvmInline
value class Stan(val value: String) {
    init {
        require(value.length == 6 && value.all(Char::isDigit)) {
            "STAN must be 6 digits, got '$value'"
        }
    }
}

/** Retrieval Reference Number returned by the switch. */
@JvmInline
value class Rrn(val value: String) {
    init {
        require(value.length == 12 && value.all(Char::isDigit)) {
            "RRN must be 12 digits, got '$value'"
        }
    }
}

@JvmInline
value class ResponseCode(val value: String) {
    init {
        require(value.isNotBlank()) { "Response code is required" }
    }
}
