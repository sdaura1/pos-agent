package com.example.pos_agent.domain.card.port

import com.example.pos_agent.core.result.Outcome
import com.example.pos_agent.domain.card.model.CardToken
import com.example.pos_agent.domain.card.model.MaskedPan
import com.example.pos_agent.domain.card.model.ResponseCode
import com.example.pos_agent.domain.card.model.Rrn
import com.example.pos_agent.domain.card.model.Stan
import com.example.pos_agent.domain.card.model.TransactionReference
import com.example.pos_agent.domain.card.model.TransactionStatus
import com.example.pos_agent.domain.shared.MerchantId
import com.example.pos_agent.domain.shared.Money
import com.example.pos_agent.domain.shared.TerminalId

/**
 * Port for the NIBSS card switch.
 *
 * The adapter in `data/remote/nibss` owns transport (ISO 8583 or REST),
 * signing, DTOs and the mapping of response codes to [TransactionStatus].
 * The domain never sees any of that.
 */
interface NibssCardGateway {
    suspend fun authorize(request: CardAuthorizationRequest): Outcome<CardOperationResult>

    suspend fun reverse(request: CardReversalRequest): Outcome<CardOperationResult>

    suspend fun refund(request: CardRefundRequest): Outcome<CardOperationResult>

    suspend fun query(reference: TransactionReference): Outcome<CardOperationResult>
}

data class CardAuthorizationRequest(
    val reference: TransactionReference,
    val stan: Stan,
    val terminalId: TerminalId,
    val merchantId: MerchantId,
    val amount: Money,
    val card: CardToken,
    val maskedPan: MaskedPan,
)

data class CardReversalRequest(
    val reference: TransactionReference,
    val originalRrn: Rrn,
    val stan: Stan,
    val terminalId: TerminalId,
    val amount: Money,
)

data class CardRefundRequest(
    val reference: TransactionReference,
    val originalRrn: Rrn,
    val stan: Stan,
    val terminalId: TerminalId,
    val amount: Money,
)

data class CardOperationResult(
    val reference: TransactionReference,
    val status: TransactionStatus,
    val responseCode: ResponseCode,
    val message: String,
    val rrn: Rrn? = null,
    val authCode: String? = null,
)
