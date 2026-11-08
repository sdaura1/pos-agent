package com.example.pos_agent.domain.card.model

/** Domain-level outcome returned by every [com.example.pos_agent.domain.card.port.NibssCardGateway] call. */
data class CardOperationResult(
    val reference: TransactionReference,
    val status: TransactionStatus,
    val responseCode: ResponseCode,
    val message: String,
    val rrn: Rrn? = null,
    val authCode: String? = null,
)
