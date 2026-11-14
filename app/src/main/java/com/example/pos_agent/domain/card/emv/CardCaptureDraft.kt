package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardCapture
import com.example.pos_agent.domain.card.model.CardEntryMode
import com.example.pos_agent.domain.card.model.CardExpiry
import com.example.pos_agent.domain.card.model.CardToken
import com.example.pos_agent.domain.card.model.CardholderVerificationMethod
import com.example.pos_agent.domain.card.model.EmvData
import com.example.pos_agent.domain.card.model.MaskedPan
import com.example.pos_agent.domain.card.model.NfcData
import com.example.pos_agent.domain.card.model.PinBlockRef

/**
 * Everything the terminal has read at the moment the kernel asks for online
 * authorisation.
 *
 * The draft is deliberately stricter than a finished [CardCapture]:
 *
 * - [emvData] is mandatory, because the machine must not emit
 *   [EmvPrompt.RequestOnline] before DE55 data is ready.
 * - a PIN-based CVM requires a [pinBlockRef].
 * - a contactless draft requires [nfcData], which NIBSS sends as DE124.
 *
 * [complete] turns the draft into the final capture once the kernel reports
 * its result code.
 */
data class CardCaptureDraft(
    val maskedPan: MaskedPan,
    val entryMode: CardEntryMode,
    val cardholderVerificationMethod: CardholderVerificationMethod,
    val cardToken: CardToken,
    val emvData: EmvData,
    val pinBlockRef: PinBlockRef? = null,
    val aid: String? = null,
    val applicationLabel: String? = null,
    val cardSerial: String? = null,
    val nfcData: NfcData? = null,
    val expiryDate: CardExpiry? = null,
    val serviceCode: String? = null,
) {
    init {
        val pinCvm = cardholderVerificationMethod == CardholderVerificationMethod.ONLINE_PIN ||
            cardholderVerificationMethod == CardholderVerificationMethod.OFFLINE_PIN
        require(!pinCvm || pinBlockRef != null) {
            "A PIN-based CVM requires a PIN block reference"
        }
        require(entryMode != CardEntryMode.CONTACTLESS || nfcData != null) {
            "A contactless capture requires NFC data"
        }
        toCapture(emvResultCode = null)
    }

    /** Builds the final capture, reusing [CardCapture]'s validation and PAN guard. */
    fun complete(emvResultCode: String? = null): CardCapture =
        toCapture(emvResultCode)

    private fun toCapture(emvResultCode: String?): CardCapture = CardCapture(
        maskedPan = maskedPan,
        entryMode = entryMode,
        cardholderVerificationMethod = cardholderVerificationMethod,
        cardToken = cardToken,
        emvData = emvData,
        pinBlockRef = pinBlockRef,
        aid = aid,
        applicationLabel = applicationLabel,
        cardSerial = cardSerial,
        emvResultCode = emvResultCode,
        nfcData = nfcData,
        expiryDate = expiryDate,
        serviceCode = serviceCode,
    )
}
