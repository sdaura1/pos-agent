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
import com.example.pos_agent.domain.shared.Money

internal fun pinBlock(): PinBlockRef = PinBlockRef(
    encryptedPinBlock = "0123456789ABCDEF",
    ksn = "01234567890123456789",
)

internal fun draft(
    entryMode: CardEntryMode = CardEntryMode.CONTACT,
    cvm: CardholderVerificationMethod = CardholderVerificationMethod.ONLINE_PIN,
    pinBlockRef: PinBlockRef? = pinBlock(),
    cardToken: CardToken = CardToken("ENCRYPTED-TOKEN"),
    nfcData: NfcData? = if (entryMode == CardEntryMode.CONTACTLESS) {
        NfcData("NFC-PAYLOAD")
    } else {
        null
    },
): CardCaptureDraft = CardCaptureDraft(
    maskedPan = MaskedPan("5061********1234"),
    entryMode = entryMode,
    cardholderVerificationMethod = cvm,
    cardToken = cardToken,
    emvData = EmvData("9F2608AABBCCDDEEFF0011"),
    pinBlockRef = pinBlockRef,
    aid = "A0000000041010",
    applicationLabel = "VISA CREDIT",
    cardSerial = "CARD-0001",
    nfcData = nfcData,
    expiryDate = CardExpiry("2912"),
    serviceCode = "201",
)

internal fun capture(
    entryMode: CardEntryMode = CardEntryMode.CONTACT,
): CardCapture = draft(entryMode = entryMode).complete("00")

internal fun money(): Money = Money.of(10_000)

internal fun cardPresented(): EmvPrompt.CardPresented =
    EmvPrompt.CardPresented(CardEntryMode.CONTACT, "CARD-0001")

internal fun selectPrompt(): EmvPrompt.SelectApplication =
    EmvPrompt.SelectApplication(
        applications = listOf("VISA CREDIT", "VERVE"),
        firstSelect = true,
    )

internal fun confirmPrompt(): EmvPrompt.ConfirmCardInfo =
    EmvPrompt.ConfirmCardInfo(
        reason = EmvPrompt.ConfirmCardInfo.Reason.AMOUNT,
        maskedPan = MaskedPan("5061********1234"),
        applicationLabel = "VISA CREDIT",
        amount = money(),
    )

internal fun pinPrompt(): EmvPrompt.RequestPin =
    EmvPrompt.RequestPin(
        mode = EmvPrompt.RequestPin.Mode.ONLINE,
        amount = money(),
        attemptsRemaining = 3,
    )

internal fun onlinePrompt(): EmvPrompt.RequestOnline =
    EmvPrompt.RequestOnline(draft())

internal fun finishedApproved(): EmvPrompt.Finished =
    EmvPrompt.Finished(EmvDecision.APPROVED, capture())

internal fun finishedDeclined(): EmvPrompt.Finished =
    EmvPrompt.Finished(EmvDecision.DECLINED, capture())
