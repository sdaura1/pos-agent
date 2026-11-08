package com.example.pos_agent.domain.card.event

import com.example.pos_agent.domain.card.model.TransactionId
import com.example.pos_agent.domain.card.model.TransactionReference
import com.example.pos_agent.domain.shared.Money

/**
 * Domain events. Persist them in the same database transaction as the
 * aggregate (outbox pattern) so reconciliation and reporting never lose an
 * approved operation, even when the device is offline.
 */
sealed interface CardTransactionEvent {
    val transactionId: TransactionId
    val reference: TransactionReference
    val amount: Money
    val atEpochMillis: Long

    data class PurchaseApproved(
        override val transactionId: TransactionId,
        override val reference: TransactionReference,
        override val amount: Money,
        override val atEpochMillis: Long,
    ) : CardTransactionEvent

    data class PurchaseDeclined(
        override val transactionId: TransactionId,
        override val reference: TransactionReference,
        override val amount: Money,
        override val atEpochMillis: Long,
        val responseCode: String,
    ) : CardTransactionEvent

    data class TransactionReversed(
        override val transactionId: TransactionId,
        override val reference: TransactionReference,
        override val amount: Money,
        override val atEpochMillis: Long,
    ) : CardTransactionEvent

    data class RefundApproved(
        override val transactionId: TransactionId,
        override val reference: TransactionReference,
        override val amount: Money,
        override val atEpochMillis: Long,
    ) : CardTransactionEvent
}
