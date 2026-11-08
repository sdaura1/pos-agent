package com.example.pos_agent.domain.card.repository

import com.example.pos_agent.core.result.Outcome
import com.example.pos_agent.domain.card.model.CardTransaction
import com.example.pos_agent.domain.card.model.TransactionId
import com.example.pos_agent.domain.card.model.TransactionReference

/**
 * Port. The implementation lives in `data/repository` (Room, SQLDelight, ...).
 */
interface CardTransactionRepository {
    suspend fun save(transaction: CardTransaction): Outcome<Unit>

    suspend fun findById(id: TransactionId): CardTransaction?

    suspend fun findByReference(reference: TransactionReference): CardTransaction?

    /** Refunds and reversals that reference [originalTransactionId]. */
    suspend fun findByOriginalId(originalTransactionId: TransactionId): List<CardTransaction>

    /** Transactions whose outcome is not final yet (INITIATED or UNKNOWN). */
    suspend fun findUnsettled(): List<CardTransaction>
}
