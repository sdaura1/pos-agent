package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money

/**
 * Aggregate root for one card operation.
 *
 * A refund or reversal is its own transaction that points at the original
 * purchase through [originalTransactionId]; the two records are never merged.
 */
data class CardTransaction(
    val id: TransactionId,
    val reference: TransactionReference,
    val terminalId: String,
    val merchantId: String,
    val type: TransactionType,
    val amount: Money,
    val stan: Stan,
    val createdAtEpochMillis: Long,
    val status: TransactionStatus = TransactionStatus.INITIATED,
    val rrn: Rrn? = null,
    val authCode: String? = null,
    val responseCode: ResponseCode? = null,
    val originalTransactionId: TransactionId? = null,
    val maskedPan: MaskedPan? = null,
    val updatedAtEpochMillis: Long = createdAtEpochMillis,
) {
    fun approve(
        rrn: Rrn,
        authCode: String?,
        responseCode: ResponseCode,
        nowEpochMillis: Long,
    ): CardTransaction = copy(
        status = TransactionStatus.APPROVED,
        rrn = rrn,
        authCode = authCode,
        responseCode = responseCode,
        updatedAtEpochMillis = nowEpochMillis,
    )

    fun decline(responseCode: ResponseCode, nowEpochMillis: Long): CardTransaction = copy(
        status = TransactionStatus.DECLINED,
        responseCode = responseCode,
        updatedAtEpochMillis = nowEpochMillis,
    )

    /** NIBSS timeout: the outcome is unknown and must be reconciled. */
    fun markUnknown(nowEpochMillis: Long): CardTransaction = copy(
        status = TransactionStatus.UNKNOWN,
        updatedAtEpochMillis = nowEpochMillis,
    )

    fun markReversed(nowEpochMillis: Long): CardTransaction = copy(
        status = TransactionStatus.REVERSED,
        updatedAtEpochMillis = nowEpochMillis,
    )

    fun markRefunded(nowEpochMillis: Long): CardTransaction = copy(
        status = TransactionStatus.REFUNDED,
        updatedAtEpochMillis = nowEpochMillis,
    )

    fun withStatus(
        status: TransactionStatus,
        responseCode: ResponseCode?,
        nowEpochMillis: Long,
    ): CardTransaction = copy(
        status = status,
        responseCode = responseCode,
        updatedAtEpochMillis = nowEpochMillis,
    )

    fun isApprovedPurchase(): Boolean =
        type == TransactionType.PURCHASE && status == TransactionStatus.APPROVED
}

enum class TransactionType { PURCHASE, REVERSAL, REFUND }

enum class TransactionStatus { INITIATED, APPROVED, DECLINED, UNKNOWN, REVERSED, REFUNDED }
