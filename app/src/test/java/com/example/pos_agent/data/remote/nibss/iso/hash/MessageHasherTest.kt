package com.example.pos_agent.data.remote.nibss.iso.hash

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageHasherTest {

    private val message = "0800...".toByteArray(Charsets.US_ASCII)
    private val sessionKey = ByteArray(16) { it.toByte() }

    @Test
    fun `the unconfigured hasher should fail closed when hash is requested`() {
        // Arrange
        val hasher = UnconfiguredMessageHasher()

        // Act
        val failure = runCatching {
            hasher.hash(message, sessionKey)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is MessageHashNotConfiguredException)
    }

    @Test
    fun `the fake hasher should be deterministic when hashing twice`() {
        // Arrange
        val hasher = FakeMessageHasher()

        // Act
        val first = hasher.hash(message, sessionKey)
        val second = hasher.hash(message, sessionKey)

        // Assert
        assertEquals(first, second)
        assertEquals(64, first.length)
        assertTrue(first.all { it in '0'..'9' || it in 'A'..'F' })
    }

    @Test
    fun `the fake hasher should change when the message or key changes`() {
        // Arrange
        val hasher = FakeMessageHasher()
        val base = hasher.hash(message, sessionKey)

        // Act
        val otherMessage = hasher.hash("0800..".toByteArray(), sessionKey)
        val otherKey = hasher.hash(message, ByteArray(16) { (it + 1).toByte() })

        // Assert
        assertNotEquals(base, otherMessage)
        assertNotEquals(base, otherKey)
    }

    @Test
    fun `the fake hasher should be named as a test double when inspected`() {
        // Arrange
        val name = FakeMessageHasher::class.java.simpleName

        // Act
        val isFake = name.startsWith("Fake")

        // Assert
        assertTrue(isFake)
    }
}
