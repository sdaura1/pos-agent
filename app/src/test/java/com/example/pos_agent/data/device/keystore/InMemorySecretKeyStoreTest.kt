package com.example.pos_agent.data.device.keystore

import com.example.pos_agent.domain.terminal.model.SecretKeyAlias
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class InMemorySecretKeyStoreTest {

    @Test
    fun `stores and returns key material`() = runTest {
        val store = InMemorySecretKeyStore()
        val key = byteArrayOf(1, 2, 3, 4)

        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, key)

        assertArrayEquals(key, store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
        assertTrue(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `missing key returns null`() = runTest {
        val store = InMemorySecretKeyStore()

        assertNull(store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
        assertFalse(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `mutating the input after put does not change the stored key`() = runTest {
        val store = InMemorySecretKeyStore()
        val key = byteArrayOf(1, 2, 3, 4)

        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, key)
        key.fill(9)

        assertArrayEquals(byteArrayOf(1, 2, 3, 4), store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `mutating the returned array does not change the stored key`() = runTest {
        val store = InMemorySecretKeyStore()
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1, 2, 3, 4))

        val returned = store.get(SecretKeyAlias.TERMINAL_SESSION_KEY)
        assertNotNull(returned)
        returned!!.fill(9)

        assertArrayEquals(byteArrayOf(1, 2, 3, 4), store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `overwrites an existing key`() = runTest {
        val store = InMemorySecretKeyStore()
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1, 2, 3, 4))

        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(5, 6))

        assertArrayEquals(byteArrayOf(5, 6), store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `different aliases are independent`() = runTest {
        val store = InMemorySecretKeyStore()
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1))
        store.put(SecretKeyAlias.TERMINAL_PIN_KEY, byteArrayOf(2))

        assertArrayEquals(byteArrayOf(1), store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
        assertArrayEquals(byteArrayOf(2), store.get(SecretKeyAlias.TERMINAL_PIN_KEY))
    }

    @Test
    fun `remove deletes the key`() = runTest {
        val store = InMemorySecretKeyStore()
        store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, byteArrayOf(1, 2, 3))

        store.remove(SecretKeyAlias.TERMINAL_SESSION_KEY)

        assertNull(store.get(SecretKeyAlias.TERMINAL_SESSION_KEY))
        assertFalse(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `removing a missing key is harmless`() = runTest {
        val store = InMemorySecretKeyStore()

        store.remove(SecretKeyAlias.TERMINAL_SESSION_KEY)

        assertFalse(store.contains(SecretKeyAlias.TERMINAL_SESSION_KEY))
    }

    @Test
    fun `refuses an empty key`() = runTest {
        val store = InMemorySecretKeyStore()

        val failure = runCatching {
            store.put(SecretKeyAlias.TERMINAL_SESSION_KEY, ByteArray(0))
        }.exceptionOrNull()

        assertTrue(failure is IllegalArgumentException)
    }
}
