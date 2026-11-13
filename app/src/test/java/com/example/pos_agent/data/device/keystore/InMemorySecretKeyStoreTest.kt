package com.example.pos_agent.data.device.keystore

import com.example.pos_agent.domain.terminal.model.SecretKeyAlias
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class InMemorySecretKeyStoreTest {

    @Test
    fun `put should store key material when a key is supplied`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()
        val key = byteArrayOf(1, 2, 3, 4)

        // Act
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, key)

        // Assert
        assertArrayEquals(key, store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
        assertTrue(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `get should return null when the key is missing`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()

        // Act
        val key = store.get(SecretKeyAlias.TERMINAL_SESSION_KEY)

        // Assert
        assertNull(key)
        assertFalse(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `the store should ignore input mutations when a key was put`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()
        val key = byteArrayOf(1, 2, 3, 4)
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, key)

        // Act
        key.fill(9)

        // Assert
        assertArrayEquals(
            byteArrayOf(1, 2, 3, 4),
            store.get(SecretKeyAlias.TERMINAL_SESSION_KEY),
        )
    }

    @Test
    fun `the store should ignore returned mutations when a key is read`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1, 2, 3, 4))
        val returned = store.get(SecretKeyAlias.TERMINAL_SESSION_KEY)
        assertNotNull(returned)

        // Act
        returned!!.fill(9)

        // Assert
        assertArrayEquals(
            byteArrayOf(1, 2, 3, 4),
            store.get(SecretKeyAlias.TERMINAL_SESSION_KEY),
        )
    }

    @Test
    fun `put should overwrite an existing key when called again`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1, 2, 3, 4))

        // Act
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(5, 6))

        // Assert
        assertArrayEquals(
            byteArrayOf(5, 6),
            store.get(SecretKeyAlias.TERMINAL_SESSION_KEY),
        )
    }

    @Test
    fun `different aliases should be independent when both are stored`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()

        // Act
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1))
        store.put(SecretKeyAlias.TERMINAL_PIN_KEY, byteArrayOf(2))

        // Assert
        assertArrayEquals(
            byteArrayOf(1),
            store.get(SecretKeyAlias.TERMINAL_SESSION_KEY),
        )
        assertArrayEquals(
            byteArrayOf(2),
            store.get(SecretKeyAlias.TERMINAL_PIN_KEY),
        )
    }

    @Test
    fun `remove should delete the key when called`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1, 2, 3))

        // Act
        store.remove(SecretKeyAlias.TERMINAL_SESSION_KEY)

        // Assert
        assertNull(store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
        assertFalse(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `remove should be harmless when the key is missing`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()

        // Act
        store.remove(SecretKeyAlias.TERMINAL_SESSION_KEY)

        // Assert
        assertFalse(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `put should reject an empty key when called`() = runTest {
        // Arrange
        val store = InMemorySecretKeyStore()

        // Act
        val failure = runCatching {
            store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, ByteArray(0))
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }
}
