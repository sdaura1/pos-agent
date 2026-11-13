package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class CardCaptureTest {

    @Test
    fun `accepts a fully populated capture`() {
        val capture = capture()

        assertTrue(capture.maskedPan.value.startsWith("5061"))
        assertTrue(capture.emvData != null)
    }

    @Test
    fun `rejects a card token that contains a readable pan`() {
        assertThrows(IllegalArgumentException::class.java) {
            capture(cardToken = CardToken("4111111111111111"))
        }
    }

    @Test
    fun `rejects a card serial that contains a readable pan`() {
        assertThrows(IllegalArgumentException::class.java) {
            capture(cardSerial = "4111111111111111")
        }
    }

    @Test
    fun `rejects an application label that contains a readable pan`() {
        assertThrows(IllegalArgumentException::class.java) {
            capture(applicationLabel = "4111111111111111")
        }
    }

    @Test
    fun `rejects an invalid aid`() {
        assertThrows(IllegalArgumentException::class.java) {
            capture(aid = "A0000000") // 4 bytes is below the AID minimum
        }
    }

    @Test
    fun `rejects an invalid service code`() {
        assertThrows(IllegalArgumentException::class.java) {
            capture(serviceCode = "20A")
        }
    }

    @Test
    fun `redacts sensitive fields from toString`() {
        val text = capture().toString()

        assertFalse(text.contains("ENCRYPTED-TOKEN"))
        assertFalse(text.contains("0123456789ABCDEF"))
        assertFalse(text.contains("9F2608AABBCCDDEEFF0011"))
        assertFalse(text.contains("NFC-PAYLOAD"))
    }

    private fun capture(
        cardToken: CardToken = CardToken("ENCRYPTED-TOKEN"),
        aid: String? = "A0000000041010",
        applicationLabel: String? = "VISA CREDIT",
        cardSerial: String = "CARD-0001",
        serviceCode: String? = "201",
    ) = CardCapture(
        maskedPan = MaskedPan("5061********1234"),
        entryMode = CardEntryMode.CONTACT,
        cardholderVerificationMethod = CardholderVerificationMethod.ONLINE_PIN,
        cardToken = cardToken,
        emvData = EmvData("9F2608AABBCCDDEEFF0011"),
        pinBlockRef = PinBlockRef(
            encryptedPinBlock = "0123456789ABCDEF",
            ksn = "01234567890123456789",
        ),
        aid = aid,
        applicationLabel = applicationLabel,
        cardSerial = cardSerial,
        emvResultCode = "00",
        nfcData = NfcData("NFC-PAYLOAD"),
        expiryDate = CardExpiry("2912"),
        serviceCode = serviceCode,
    )
}
