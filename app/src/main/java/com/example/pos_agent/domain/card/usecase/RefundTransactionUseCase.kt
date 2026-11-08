package com.example.pos_agent.domain.card.usecase

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Outcome
import com.example.pos_agent.core.time.Clock
import com.example.pos_agent.domain.card.model.CardRefundRequest
import com.example.pos_agent.domain.card.model.CardTransaction
import com.example.pos_agent.domain.card.model.Stan
import com.example.pos_agent.domain.card.model.TransactionId
import com.example.pos_agent.domain.card.model.TransactionReference
import com.example.pos_agent.domain.card.model.TransactionStatus
import com.example.pos_agent.domain.card.model.TransactionType
import com.example.pos_agent.domain.card.port.NibssCardGateway
import com.example.pos_agent.domain.card.repository.CardTransactionRepository
import com.example.pos_agent.domain.shared.Money
import java.util.UUID

/**
 * Refund: return all or part of an approved purchase.
 * Partial refunds are allowed; the sum of approved refunds can never exceed
 * the original amount.
 */
class RefundTransactionUseCase(
    private val transactions: CardTransactionRepository,
    private val gateway: NibssCardGateway,
    private val clock: Clock,
) {
    suspend operator fun invoke(command: RefundCommand): Outcome<CardTransaction> {
        val original = transactions.findById(command.originalTransactionId)
            ?: return Outcome.Failure(
                DomainError.NotFound("Transaction ${command.originalTransactionId.value} not found")
            )

        if (!original.isApprovedPurchase()) {
            return Outcome.Failure(
                DomainError.Validation("Only an approved purchase can be refunded")
            )
        }

        val originalRrn = original.rrn
            ?: return Outcome.Failure(
                DomainError.Conflict("Original transaction has no RRN to refund")
            )

        if (command.amount > original.amount) {
            return Outcome.Failure(
                DomainError.Validation("Refund amount exceeds the original amount")
            )
        }

        val alreadyRefunded = transactions.findByOriginalId(original.id)
            .filter { it.type == TransactionType.REFUND && it.status == TransactionStatus.APPROVED }
            .fold(Money.zero(original.amount.currency)) { total, refund -> total + refund.amount }

        if (command.amount > original.amount - alreadyRefunded) {
            return Outcome.Failure(
                DomainError.Validation("Refund exceeds the remaining refundable amount")
            )
        }

        val refund = CardTransaction(
            id = TransactionId(UUID.randomUUID().toString()),
            reference = command.reference,
            terminalId = original.terminalId,
            merchantId = original.merchantId,
            type = TransactionType.REFUND,
            amount = command.amount,
            stan = command.stan,
            originalTransactionId = original.id,
            createdAtEpochMillis = clock.nowEpochMillis(),
        )

        when (val saved = transactions.save(refund)) {
            is Outcome.Failure -> return saved
            is Outcome.Success -> Unit
        }

        val result = gateway.refund(
            CardRefundRequest(
                reference = refund.reference,
                originalRrn = originalRrn,
                stan = refund.stan,
                terminalId = refund.terminalId,
                amount = refund.amount,
            )
        )

        return when (result) {
            is Outcome.Success -> {
                val now = clock.nowEpochMillis()
                val approved = result.value.status == TransactionStatus.APPROVED
                val updatedRefund = if (approved) {
                    refund.approve(
                        rrn = result.value.rrn ?: originalRrn,
                        authCode = result.value.authCode,
                        responseCode = result.value.responseCode,
                        nowEpochMillis = now,
                    )
                } else {
                    refund.withStatus(result.value.status, result.value.responseCode, now)
                }
                transactions.save(updatedRefund)

                // Only a fully refunded purchase changes status; partial refunds
                // stay APPROVED and the refund history carries the detail.
                if (approved && alreadyRefunded + command.amount == original.amount) {
                    transactions.save(original.markRefunded(now))
                }
                Outcome.Success(updatedRefund)
            }

            is Outcome.Failure -> {
                val status = if (result.error is DomainError.Network) {
                    TransactionStatus.UNKNOWN
                } else {
                    TransactionStatus.DECLINED
                }
                transactions.save(
                    refund.withStatus(status, null, clock.nowEpochMillis())
                )
                Outcome.Failure(result.error)
            }
        }
    }
}

data class RefundCommand(
    val originalTransactionId: TransactionId,
    val reference: TransactionReference,
    val stan: Stan,
    val amount: Money,
)
