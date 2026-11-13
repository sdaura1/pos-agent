package com.example.pos_agent.data.remote.nibss.iso.security

import com.example.pos_agent.data.remote.nibss.iso.HexCodec
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Key Check Value for DES and triple-DES keys.
 *
 * KCV is the first three bytes of the key encrypting an eight-byte zero block,
 * rendered as six uppercase hex characters. It proves a key was loaded without
 * revealing it.
 *
 * Supported key lengths: 8 (single DES), 16 (two-key 3DES) and 24 (three-key
 * 3DES). AES keys use a different KCV scheme and are rejected on purpose.
 */
object KeyCheckValue {

    private val ZERO_BLOCK = ByteArray(8)

    fun calculate(key: ByteArray): String = HexCodec.encode(calculateBytes(key))

    fun calculateBytes(key: ByteArray): ByteArray {
        val expanded = when (key.size) {
            8 -> key
            16 -> key + key.copyOf(8) // two-key 3DES is K1||K2||K1
            24 -> key
            else -> throw IllegalArgumentException(
                "KCV requires an 8-, 16- or 24-byte DES key, got ${key.size} bytes"
            )
        }
        val algorithm = if (expanded.size == 8) "DES" else "DESede"
        val cipher = Cipher.getInstance("$algorithm/ECB/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(expanded, algorithm))
        return cipher.doFinal(ZERO_BLOCK).copyOf(3)
    }

    /** Case-insensitive, constant-time comparison of two KCV strings. */
    fun matches(expected: String, actual: String): Boolean =
        HexCodec.constantTimeEquals(expected, actual)
}
