package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money

/** Domain-level request for [com.example.pos_agent.domain.card.port.NibssCardGateway.authorize]. */
data class CardAuthorizationRequest(
    val reference: TransactionReference,
    val stan: Stan,
    val terminalId: String,
    val merchantId: String,
    val amount: Money,
    val card: CardToken,
    val maskedPan: MaskedPan,
)
