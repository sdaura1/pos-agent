package com.example.pos_agent.data.remote.nibss.iso.hash

/**
 * Computes the DE64/DE128 message hash.
 *
 * The interface is deliberately independent of the algorithm: NIBSS specifies
 * SHA-256 seeded with the terminal session key, but not the exact composition
 * (field order, padding, encoding). Until that is confirmed, bind
 * [UnconfiguredMessageHasher] so the app fails closed, and swap in the
 * certified implementation when the vector arrives.
 */
fun interface MessageHasher {
    /** Returns the hash as uppercase hex, formatted for DE64 or DE128. */
    fun hash(message: ByteArray, sessionKey: ByteArray): String
}

/** Raised when a hash is requested before the algorithm is configured. */
class MessageHashNotConfiguredException : IllegalStateException(
    "CTMS message hash composition is not configured; awaiting the NIBSS definition"
)

/**
 * Fail-closed default. Sending a message with a guessed hash is worse than
 * refusing to send it.
 */
class UnconfiguredMessageHasher : MessageHasher {
    override fun hash(message: ByteArray, sessionKey: ByteArray): String =
        throw MessageHashNotConfiguredException()
}
