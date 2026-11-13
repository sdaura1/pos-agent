package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CardCaptureTest {

    @Test
    fun `CardCapture should accept a populated capture when constructed`() {
        // Arrange
        val capture = capture()

        // Act
        val maskedPan = capture.maskedPan.value
        val emvData = capture.emvData

        // Assert
        assertTrue(maskedPan.startsWith("5061"))
        assertTrue(emvData != null)
    }

    @Test
    fun `CardCapture should reject a PAN in the token when constructed`() {
        // Arrange
        val pan = "4111111111111111"

        // Act
        val failure = runCatching {
            capture(cardToken = CardToken(pan))
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `CardCapture should reject a PAN in the serial when constructed`() {
        // Arrange
        val pan = "4111111111111111"

        // Act
        val failure = runCatching {
            capture(cardSerial = pan)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `CardCapture should reject a PAN in the label when constructed`() {
        // Arrange
        val pan = "4111111111111111"

        // Act
        val failure = runCatching {
            capture(applicationLabel = pan)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `CardCapture should reject a short AID when constructed`() {
        // Arrange
        val shortAid = "A0000000"

        // Act
        val failure = runCatching {
            capture(aid = shortAid)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `CardCapture should reject a non-numeric service code when built`() {
        // Arrange
        val invalidCode = "20A"

        // Act
        val failure = runCatching {
            capture(serviceCode = invalidCode)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `CardCapture should redact sensitive fields when converted to text`() {
        // Arrange
        val capture = capture()

        // Act
        val text = capture.toString()

        // Assert
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
