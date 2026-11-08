package com.example.pos_agent.domain.card.usecase

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.core.time.Clock
import com.example.pos_agent.domain.card.model.CardReversalRequest
import com.example.pos_agent.domain.card.model.CardTransaction
import com.example.pos_agent.domain.card.model.Stan
import com.example.pos_agent.domain.card.model.TransactionId
import com.example.pos_agent.domain.card.model.TransactionReference
import com.example.pos_agent.domain.card.model.TransactionStatus
import com.example.pos_agent.domain.card.model.TransactionType
import com.example.pos_agent.domain.card.port.NibssCardGateway
import com.example.pos_agent.domain.card.repository.CardTransactionRepository
import java.util.UUID

/**
 * Reversal: cancel an approved purchase, typically in the same batch/day.
 * A reversal is stored as a separate transaction linked to the original.
 */
class ReverseTransactionUseCase(
    private val transactions: CardTransactionRepository,
    private val gateway: NibssCardGateway,
    private val clock: Clock,
) {
    suspend operator fun invoke(command: ReversalCommand): Response<CardTransaction> {
        val original = transactions.findById(command.originalTransactionId)
            ?: return Response.Failure(
                DomainError.NotFound("Transaction ${command.originalTransactionId.value} not found")
            )

        if (!original.isApprovedPurchase()) {
            return Response.Failure(
                DomainError.Validation("Only an approved purchase can be reversed")
            )
        }

        val originalRrn = original.rrn
            ?: return Response.Failure(
                DomainError.Conflict("Original transaction has no RRN to reverse")
            )

        val reversal = CardTransaction(
            id = TransactionId(UUID.randomUUID().toString()),
            reference = command.reference,
            terminalId = original.terminalId,
            merchantId = original.merchantId,
            type = TransactionType.REVERSAL,
            amount = original.amount,
            stan = command.stan,
            originalTransactionId = original.id,
            createdAtEpochMillis = clock.nowEpochMillis(),
        )

        when (val saved = transactions.save(reversal)) {
            is Response.Failure -> return saved
            is Response.Success -> Unit
        }

        val result = gateway.reverse(
            CardReversalRequest(
                reference = reversal.reference,
                originalRrn = originalRrn,
                stan = reversal.stan,
                terminalId = reversal.terminalId,
                amount = reversal.amount,
            )
        )

        return when (result) {
            is Response.Success -> {
                val now = clock.nowEpochMillis()
                val approved = result.value.status == TransactionStatus.APPROVED
                val updatedReversal = if (approved) {
                    reversal.approve(
                        rrn = result.value.rrn ?: originalRrn,
                        authCode = result.value.authCode,
                        responseCode = result.value.responseCode,
                        nowEpochMillis = now,
                    )
                } else {
                    reversal.withStatus(result.value.status, result.value.responseCode, now)
                }
                transactions.save(updatedReversal)
                if (approved) transactions.save(original.markReversed(now))
                Response.Success(updatedReversal)
            }

            is Response.Failure -> {
                val status = if (result.error is DomainError.Network) {
                    TransactionStatus.UNKNOWN
                } else {
                    TransactionStatus.DECLINED
                }
                transactions.save(
                    reversal.withStatus(status, null, clock.nowEpochMillis())
                )
                Response.Failure(result.error)
            }
        }
    }
}

data class ReversalCommand(
    val originalTransactionId: TransactionId,
    val reference: TransactionReference,
    val stan: Stan,
)
