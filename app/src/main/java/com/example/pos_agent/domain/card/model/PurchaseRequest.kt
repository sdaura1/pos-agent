package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money

/** Input required to start a card purchase. */
data class PurchaseRequest(
    val reference: TransactionReference,
    val terminalId: String,
    val merchantId: String,
    val amount: Money,
    val stan: Stan,
    val card: CardToken,
    val maskedPan: MaskedPan,
)
