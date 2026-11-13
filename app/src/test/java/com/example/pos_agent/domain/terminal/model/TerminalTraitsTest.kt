package com.example.pos_agent.domain.terminal.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class TerminalTraitsTest {

    @Test
    fun `defaults to an attended card-acceptor pos`() {
        val traits = TerminalTraits()

        assertEquals(PinEntryCapability.UNKNOWN, traits.pinEntryCapability)
        assertEquals(TerminalOperator.CARD_ACCEPTOR, traits.operator)
        assertEquals("01", traits.terminalTypeCode)
        assertEquals(null, traits.maxPinLength)
    }

    @Test
    fun `accepts the full pin length range`() {
        TerminalTraits(maxPinLength = 4)
        TerminalTraits(maxPinLength = 12)
    }

    @Test
    fun `rejects a pin length outside 4 to 12`() {
        assertThrows(IllegalArgumentException::class.java) { TerminalTraits(maxPinLength = 3) }
        assertThrows(IllegalArgumentException::class.java) { TerminalTraits(maxPinLength = 13) }
    }

    @Test
    fun `rejects an invalid terminal type code`() {
        assertThrows(IllegalArgumentException::class.java) { TerminalTraits(terminalTypeCode = "1") }
        assertThrows(IllegalArgumentException::class.java) { TerminalTraits(terminalTypeCode = "AB") }
    }
}
