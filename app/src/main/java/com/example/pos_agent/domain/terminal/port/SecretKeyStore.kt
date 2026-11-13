package com.example.pos_agent.domain.terminal.port

import com.example.pos_agent.domain.terminal.model.SecretKeyAlias

/**
 * Storage for terminal key material.
 *
 * Contract:
 * - Implementations must copy on write and copy on read; callers own the
 *   returned array and may zero it.
 * - Removing a key overwrites the stored bytes before dropping them.
 * - Keys are never logged, serialised to preferences in clear, or included in
 *   backups.
 */
interface SecretKeyStore {
    suspend fun put(alias: SecretKeyAlias, key: ByteArray)

    /** Returns a copy of the stored key, or `null` when it is absent. */
    suspend fun get(alias: SecretKeyAlias): ByteArray?

    suspend fun remove(alias: SecretKeyAlias)

    suspend fun contains(alias: SecretKeyAlias): Boolean
}
