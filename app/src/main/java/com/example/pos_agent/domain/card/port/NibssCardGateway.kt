package com.example.pos_agent.domain.card.port

import com.example.pos_agent.core.result.Outcome
import com.example.pos_agent.domain.card.model.CardAuthorizationRequest
import com.example.pos_agent.domain.card.model.CardOperationResult
import com.example.pos_agent.domain.card.model.CardRefundRequest
import com.example.pos_agent.domain.card.model.CardReversalRequest
import com.example.pos_agent.domain.card.model.TransactionReference
import com.example.pos_agent.domain.card.model.TransactionStatus

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
