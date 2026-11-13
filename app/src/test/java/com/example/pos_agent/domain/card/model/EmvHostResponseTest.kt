package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmvHostResponseTest {

    @Test
    fun `EmvHostResponse should accept auth data and two scripts when built`() {
        // Arrange
        val authData = IssuerAuthenticationData(EmvData("AABBCCDD"))
        val scripts = listOf(
            IssuerScript(IssuerScriptTag.SCRIPT_71, EmvData("7101AA")),
            IssuerScript(IssuerScriptTag.SCRIPT_72, EmvData("7201BB")),
        )

        // Act
        val response = EmvHostResponse(
            issuerAuthenticationData = authData,
            issuerScripts = scripts,
        )

        // Assert
        assertEquals(2, response.issuerScripts.size)
        assertTrue(response.issuerAuthenticationData != null)
    }

    @Test
    fun `EmvHostResponse should reject more than two scripts when built`() {
        // Arrange
        val scripts = listOf(
            IssuerScript(IssuerScriptTag.SCRIPT_71, EmvData("7101AA")),
            IssuerScript(IssuerScriptTag.SCRIPT_72, EmvData("7201BB")),
            IssuerScript(IssuerScriptTag.SCRIPT_71, EmvData("7101CC")),
        )

        // Act
        val failure = runCatching {
            EmvHostResponse(issuerScripts = scripts)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `IssuerAuthenticationData should redact its value when converted`() {
        // Arrange
        val authData = IssuerAuthenticationData(EmvData("AABBCCDD"))

        // Act
        val text = authData.toString()

        // Assert
        assertEquals("IssuerAuthenticationData(***)", text)
    }
}
