package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class EmvDataTest {

    @Test
    fun `accepts uppercase and lowercase hex`() {
        assertEquals("9F2608AABB", EmvData("9F2608AABB").value)
        assertEquals("9f2608aabb", EmvData("9f2608aabb").value)
    }

    @Test
    fun `rejects an odd number of hex characters`() {
        assertThrows(IllegalArgumentException::class.java) { EmvData("9F2608AAB") }
    }

    @Test
    fun `rejects non-hex characters`() {
        assertThrows(IllegalArgumentException::class.java) { EmvData("9F26GG") }
    }

    @Test
    fun `rejects an empty value`() {
        assertThrows(IllegalArgumentException::class.java) { EmvData("") }
    }

    @Test
    fun `redacts the value from toString`() {
        assertEquals("EmvData(***)", EmvData("9F2608AABB").toString())
    }
}
