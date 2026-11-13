package com.example.pos_agent.data.remote.nibss.iso.hash

import com.example.pos_agent.data.remote.nibss.iso.Sha256

/**
 * Deterministic hash for fixtures and tests only.
 *
 * It uses SHA-256 over `sessionKey || message`, which is a plausible shape but
 * **not** the confirmed NIBSS composition. Never bind this for live traffic.
 */
class FakeMessageHasher : MessageHasher {
    override fun hash(message: ByteArray, sessionKey: ByteArray): String =
        Sha256.hexDigest(sessionKey + message)
}
