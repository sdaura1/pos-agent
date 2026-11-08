package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money

/** Domain-level request for [com.example.pos_agent.domain.card.port.NibssCardGateway.reverse]. */
data class CardReversalRequest(
    val reference: TransactionReference,
    val originalRrn: Rrn,
    val stan: Stan,
    val terminalId: String,
    val amount: Money,
)
