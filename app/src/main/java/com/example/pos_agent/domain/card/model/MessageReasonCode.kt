package com.example.pos_agent.domain.card.model

/**
 * ISO 8583 message reason code (DE56), used on reversals and advices.
 *
 * The two values this app sends are cancellation and timeout; the class stays
 * open because the host may require others.
 */
@JvmInline
value class MessageReasonCode(val value: String) {
    init {
        require(isDigits(value, 4)) { "Message reason code must be four digits" }
    }

    companion object {
        val CUSTOMER_CANCELLATION = MessageReasonCode("4000")
        val TIMEOUT = MessageReasonCode("4021")
    }
}
