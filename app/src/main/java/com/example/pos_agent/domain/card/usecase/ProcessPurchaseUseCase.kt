package com.example.pos_agent.domain.card.usecase

import com.example.pos_agent.core.error.DomainError
import com.example.pos_agent.core.result.Outcome
import com.example.pos_agent.core.time.Clock
import com.example.pos_agent.domain.card.model.CardAuthorizationRequest
import com.example.pos_agent.domain.card.model.CardOperationResult
import com.example.pos_agent.domain.card.model.CardToken
import com.example.pos_agent.domain.card.model.CardTransaction
import com.example.pos_agent.domain.card.model.MaskedPan
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
    suspend operator fun invoke(command: PurchaseCommand): Outcome<CardTransaction> {
        val transaction = CardTransaction(
            id = TransactionId(UUID.randomUUID().toString()),
            reference = command.reference,
            terminalId = command.terminalId,
            merchantId = command.merchantId,
            type = TransactionType.PURCHASE,
            amount = command.amount,
            stan = command.stan,
            maskedPan = command.maskedPan,
            createdAtEpochMillis = clock.nowEpochMillis(),
        )

        when (val saved = transactions.save(transaction)) {
            is Outcome.Failure -> return saved
            is Outcome.Success -> Unit
        }

        val result = gateway.authorize(
            CardAuthorizationRequest(
                reference = transaction.reference,
                stan = transaction.stan,
                terminalId = transaction.terminalId,
                merchantId = transaction.merchantId,
                amount = transaction.amount,
                card = command.card,
                maskedPan = command.maskedPan,
            )
        )
        return applyResult(transaction, result)
    }

    private suspend fun applyResult(
        transaction: CardTransaction,
        result: Outcome<CardOperationResult>,
    ): Outcome<CardTransaction> = when (result) {
        is Outcome.Success -> {
            val outcome = result.value
            val now = clock.nowEpochMillis()
            val approvedRrn = outcome.rrn
            val updated = when {
                outcome.status == TransactionStatus.APPROVED && approvedRrn != null ->
                    transaction.approve(
                        rrn = approvedRrn,
                        authCode = outcome.authCode,
                        responseCode = outcome.responseCode,
                        nowEpochMillis = now,
                    )

                // Approved without an RRN cannot be reconciled; treat it as unknown.
                outcome.status == TransactionStatus.APPROVED -> transaction.markUnknown(now)

                outcome.status == TransactionStatus.DECLINED ->
                    transaction.decline(outcome.responseCode, now)

                else -> transaction.markUnknown(now)
            }
            transactions.save(updated)
            Outcome.Success(updated)
        }

        is Outcome.Failure -> {
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
            Outcome.Failure(result.error)
        }
    }
}

data class PurchaseCommand(
    val reference: TransactionReference,
    val terminalId: String,
    val merchantId: String,
    val amount: Money,
    val stan: Stan,
    val card: CardToken,
    val maskedPan: MaskedPan,
)
