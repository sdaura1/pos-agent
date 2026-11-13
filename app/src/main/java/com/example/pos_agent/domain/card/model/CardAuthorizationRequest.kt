package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money

/**
 * Domain-level request for [com.example.pos_agent.domain.card.port.NibssCardGateway.authorize].
 *
 * The capture fields are populated by the acquisition flow (S1.3). They are
 * nullable until that flow is wired; the NIBSS adapter must reject a request
 * that is missing a mandatory host field rather than guessing one.
 */
data class CardAuthorizationRequest(
    val reference: TransactionReference,
    val stan: Stan,
    val terminalId: String,
    val merchantId: String,
    val amount: Money,
    val card: CardToken,
    val maskedPan: MaskedPan,
    val entryMode: CardEntryMode? = null,
    val cardholderVerificationMethod: CardholderVerificationMethod? = null,
    val emvData: EmvData? = null,
    val pinBlockRef: PinBlockRef? = null,
    val aid: String? = null,
    val applicationLabel: String? = null,
    val nfcData: NfcData? = null,
    val geoCoordinates: GeoCoordinates? = null,
)
