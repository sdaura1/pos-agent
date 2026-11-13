package com.example.pos_agent.data.device.keystore

import com.example.pos_agent.domain.terminal.model.SecretKeyAlias
import com.example.pos_agent.domain.terminal.port.SecretKeyStore
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Process-local key store for development and tests.
 *
 * This is the honest implementation until the TSK storage decision (memory-only
 * versus encrypted at rest) is made. It is deliberately not persisted: a
 * restart requires a fresh sign-on and key download.
 */
class InMemorySecretKeyStore : SecretKeyStore {

    private val mutex = Mutex()
    private val keys = mutableMapOf<SecretKeyAlias, ByteArray>()

    override suspend fun put(alias: SecretKeyAlias, key: ByteArray) {
        require(key.isNotEmpty()) { "Refusing to store an empty key for $alias" }
        mutex.withLock {
            keys.put(alias, key.copyOf())?.fill(0)
        }
    }

    override suspend fun get(alias: SecretKeyAlias): ByteArray? = mutex.withLock {
        keys[alias]?.copyOf()
    }

    override suspend fun remove(alias: SecretKeyAlias) {
        mutex.withLock {
            keys.remove(alias)?.fill(0)
        }
    }

    override suspend fun contains(alias: SecretKeyAlias): Boolean = mutex.withLock {
        keys.containsKey(alias)
    }
}
