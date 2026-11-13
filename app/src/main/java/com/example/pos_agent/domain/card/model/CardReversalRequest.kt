package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money

/**
 * Domain-level request for [com.example.pos_agent.domain.card.port.NibssCardGateway.reverse].
 *
 * [messageReasonCode] and [replacementAmounts] are mandatory on the NIBSS 0420
 * message. They stay nullable here until the reversal flow supplies them; the
 * adapter must not invent them.
 */
data class CardReversalRequest(
    val reference: TransactionReference,
    val originalRrn: Rrn,
    val stan: Stan,
    val terminalId: String,
    val amount: Money,
    val messageReasonCode: MessageReasonCode? = null,
    val replacementAmounts: ReplacementAmounts? = null,
)
