package com.example.pos_agent.data.remote.nibss.iso.hash

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageHasherTest {

    private val message = "0800...".toByteArray(Charsets.US_ASCII)
    private val sessionKey = ByteArray(16) { it.toByte() }

    @Test
    fun `the unconfigured hasher fails closed`() {
        assertThrows(MessageHashNotConfiguredException::class.java) {
            UnconfiguredMessageHasher().hash(message, sessionKey)
        }
    }

    @Test
    fun `the fake is deterministic and formatted as a de128 value`() {
        val hasher = FakeMessageHasher()

        val first = hasher.hash(message, sessionKey)
        val second = hasher.hash(message, sessionKey)

        assertEquals(first, second)
        assertEquals(64, first.length)
        assertTrue(first.all { it in '0'..'9' || it in 'A'..'F' })
    }

    @Test
    fun `the fake changes when the message or session key changes`() {
        val hasher = FakeMessageHasher()

        val base = hasher.hash(message, sessionKey)

        assertNotEquals(base, hasher.hash("0800..".toByteArray(), sessionKey))
        assertNotEquals(base, hasher.hash(message, ByteArray(16) { (it + 1).toByte() }))
    }

    @Test
    fun `the fake is documented as test only`() {
        // Guard against accidental production use: the class name starts with Fake.
        assertTrue(FakeMessageHasher::class.java.simpleName.startsWith("Fake"))
    }
}
