package com.example.pos_agent.domain.card.model

/**
 * Everything the terminal read for one card presentation.
 *
 * Capture is separate from authorisation: this type says what was read, while
 * [CardOperationResult] says what the host decided.
 *
 * [cardToken], [emvData], [pinBlockRef] and [nfcData] are redacted from
 * `toString`. [emvData] and [nfcData] can contain sensitive EMV tags, so they
 * must never be logged or persisted as readable values.
 */
data class CardCapture(
    val maskedPan: MaskedPan,
    val entryMode: CardEntryMode,
    val cardholderVerificationMethod: CardholderVerificationMethod,
    val cardToken: CardToken,
    val emvData: EmvData? = null,
    val pinBlockRef: PinBlockRef? = null,
    val aid: String? = null,
    val applicationLabel: String? = null,
    val cardSerial: String? = null,
    val emvResultCode: String? = null,
    val nfcData: NfcData? = null,
    val expiryDate: CardExpiry? = null,
    val serviceCode: String? = null,
) {
    init {
        require(aid == null || isHex(aid)) {
            "AID must be an even-length hex string"
        }
        require(aid == null || aid.length in 10..32) {
            "AID must be 5 to 16 bytes"
        }
        require(applicationLabel == null || applicationLabel.length <= 16) {
            "Application label cannot exceed 16 characters"
        }
        require(cardSerial == null || cardSerial.length <= 64) {
            "Card serial is too long"
        }
        require(emvResultCode == null || emvResultCode.length <= 4) {
            "EMV result code is too long"
        }
        require(serviceCode == null || isDigits(serviceCode, 3)) {
            "Service code must be three digits"
        }

        val readable = listOfNotNull(
            cardToken.value,
            applicationLabel,
            cardSerial,
            emvResultCode,
            serviceCode,
        )
        require(readable.none(::containsPanLike)) {
            "CardCapture must not contain a readable PAN"
        }
    }
}
