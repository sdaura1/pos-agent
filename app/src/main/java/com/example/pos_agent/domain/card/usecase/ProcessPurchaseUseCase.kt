package com.example.pos_agent.domain.card.usecase

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.core.time.Clock
import com.example.pos_agent.domain.card.model.CardAuthorizationRequest
import com.example.pos_agent.domain.card.model.CardOperationResult
import com.example.pos_agent.domain.card.model.CardTransaction
import com.example.pos_agent.domain.card.model.PurchaseRequest
import com.example.pos_agent.domain.card.model.TransactionId
import com.example.pos_agent.domain.card.model.TransactionStatus
import com.example.pos_agent.domain.card.model.TransactionType
import com.example.pos_agent.domain.card.port.NibssCardGateway
import com.example.pos_agent.domain.card.repository.CardTransactionRepository
import java.util.UUID

/**
 * Card purchase: authorise a card, then persist the outcome.
 *
 * The local record is written before the network call so a crash or timeout
 * leaves a trace that can be reconciled.
 */
class ProcessPurchaseUseCase(
    private val transactions: CardTransactionRepository,
    private val gateway: NibssCardGateway,
    private val clock: Clock,
) {
    suspend operator fun invoke(request: PurchaseRequest): Response<CardTransaction> {
        val transaction = CardTransaction(
            id = TransactionId(UUID.randomUUID().toString()),
            reference = request.reference,
            terminalId = request.terminalId,
            merchantId = request.merchantId,
            type = TransactionType.PURCHASE,
            amount = request.amount,
            stan = request.stan,
            maskedPan = request.maskedPan,
            createdAtEpochMillis = clock.nowEpochMillis(),
        )

        when (val saved = transactions.save(transaction)) {
            is Response.Failure -> return saved
            is Response.Success -> Unit
        }

        val result = gateway.authorize(
            CardAuthorizationRequest(
                reference = transaction.reference,
                stan = transaction.stan,
                terminalId = transaction.terminalId,
                merchantId = transaction.merchantId,
                amount = transaction.amount,
                card = request.card,
                maskedPan = request.maskedPan,
            )
        )
        return applyResult(transaction, result)
    }

    private suspend fun applyResult(
        transaction: CardTransaction,
        result: Response<CardOperationResult>,
    ): Response<CardTransaction> = when (result) {
        is Response.Success -> {
            val response = result.value
            val now = clock.nowEpochMillis()
            val approvedRrn = response.rrn
            val updated = when {
                response.status == TransactionStatus.APPROVED && approvedRrn != null ->
                    transaction.approve(
                        rrn = approvedRrn,
                        authCode = response.authCode,
                        responseCode = response.responseCode,
                        nowEpochMillis = now,
                    )

                // Approved without an RRN cannot be reconciled; treat it as unknown.
                response.status == TransactionStatus.APPROVED -> transaction.markUnknown(now)

                response.status == TransactionStatus.DECLINED ->
                    transaction.decline(response.responseCode, now)

                else -> transaction.markUnknown(now)
            }
            transactions.save(updated)
            Response.Success(updated)
        }

        is Response.Failure -> {
            // A transport failure is not proof of decline: NIBSS may have
            // processed the request. Mark UNKNOWN and reconcile later.
            val status = if (result.error is DomainError.Network) {
                TransactionStatus.UNKNOWN
            } else {
                TransactionStatus.DECLINED
            }
            val updated = transaction.withStatus(
                status = status,
                responseCode = null,
                nowEpochMillis = clock.nowEpochMillis(),
            )
            transactions.save(updated)
            Response.Failure(result.error)
        }
    }
}
