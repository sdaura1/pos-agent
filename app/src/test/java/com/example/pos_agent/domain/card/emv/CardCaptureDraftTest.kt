package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardEntryMode
import com.example.pos_agent.domain.card.model.CardToken
import com.example.pos_agent.domain.card.model.CardholderVerificationMethod
import com.example.pos_agent.domain.card.model.NfcData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CardCaptureDraftTest {

    @Test
    fun `complete should attach the result code when the draft is valid`() {
        // Arrange
        val draft = draft()

        // Act
        val capture = draft.complete("00")

        // Assert
        assertEquals("00", capture.emvResultCode)
        assertEquals(draft.maskedPan, capture.maskedPan)
        assertEquals(draft.emvData, capture.emvData)
    }

    @Test
    fun `CardCaptureDraft should reject a pin cvm when no pin block is present`() {
        // Arrange
        val cvm = CardholderVerificationMethod.ONLINE_PIN

        // Act
        val failure = runCatching {
            draft(cvm = cvm, pinBlockRef = null)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `CardCaptureDraft should accept a signature cvm when no pin block is present`() {
        // Arrange
        val cvm = CardholderVerificationMethod.UNKNOWN

        // Act
        val draft = draft(cvm = cvm, pinBlockRef = null)

        // Assert
        assertNotNull(draft)
        assertEquals(null, draft.pinBlockRef)
    }

    @Test
    fun `CardCaptureDraft should reject contactless data when NFC is missing`() {
        // Arrange
        val entryMode = CardEntryMode.CONTACTLESS

        // Act
        val failure = runCatching {
            draft(entryMode = entryMode, nfcData = null)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `CardCaptureDraft should keep NFC data when the card is contactless`() {
        // Arrange
        val entryMode = CardEntryMode.CONTACTLESS

        // Act
        val draft = draft(entryMode = entryMode, nfcData = NfcData("NFC-PAYLOAD"))

        // Assert
        assertEquals("NFC-PAYLOAD", draft.nfcData?.value)
    }

    @Test
    fun `CardCaptureDraft should reject a PAN in the token when constructed`() {
        // Arrange
        val pan = "4111111111111111"

        // Act
        val failure = runCatching {
            draft(cardToken = CardToken(pan))
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `complete should reject a result code when it exceeds four characters`() {
        // Arrange
        val draft = draft()

        // Act
        val failure = runCatching { draft.complete("12345") }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }
}
