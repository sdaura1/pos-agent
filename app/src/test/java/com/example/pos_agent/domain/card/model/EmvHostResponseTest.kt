package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class EmvHostResponseTest {

    @Test
    fun `accepts authentication data and two scripts`() {
        val response = EmvHostResponse(
            issuerAuthenticationData = IssuerAuthenticationData(EmvData("AABBCCDD")),
            issuerScripts = listOf(
                IssuerScript(IssuerScriptTag.SCRIPT_71, EmvData("7101AA")),
                IssuerScript(IssuerScriptTag.SCRIPT_72, EmvData("7201BB")),
            ),
        )

        assertEquals(2, response.issuerScripts.size)
        assertTrue(response.issuerAuthenticationData != null)
    }

    @Test
    fun `rejects more than two issuer scripts`() {
        assertThrows(IllegalArgumentException::class.java) {
            EmvHostResponse(
                issuerScripts = listOf(
                    IssuerScript(IssuerScriptTag.SCRIPT_71, EmvData("7101AA")),
                    IssuerScript(IssuerScriptTag.SCRIPT_72, EmvData("7201BB")),
                    IssuerScript(IssuerScriptTag.SCRIPT_71, EmvData("7101CC")),
                ),
            )
        }
    }

    @Test
    fun `redacts issuer authentication data from toString`() {
        val text = IssuerAuthenticationData(EmvData("AABBCCDD")).toString()

        assertEquals("IssuerAuthenticationData(***)", text)
    }
}
