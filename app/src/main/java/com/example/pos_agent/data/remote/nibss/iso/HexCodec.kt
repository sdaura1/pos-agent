package com.example.pos_agent.data.remote.nibss.iso

import java.security.MessageDigest

/** Hex encoding/decoding used by key check values and tests. */
internal object HexCodec {

    private val DIGITS = "0123456789ABCDEF".toCharArray()

    fun encode(bytes: ByteArray): String {
        val output = CharArray(bytes.size * 2)
        bytes.forEachIndexed { index, byte ->
            val value = byte.toInt() and 0xFF
            output[index * 2] = DIGITS[value ushr 4]
            output[index * 2 + 1] = DIGITS[value and 0x0F]
        }
        return String(output)
    }

    /** Returns `null` for odd-length or non-hex input instead of throwing. */
    fun decodeOrNull(value: String): ByteArray? {
        if (value.length % 2 != 0) return null
        val output = ByteArray(value.length / 2)
        for (index in output.indices) {
            val high = Character.digit(value[index * 2], 16)
            val low = Character.digit(value[index * 2 + 1], 16)
            if (high < 0 || low < 0) return null
            output[index] = ((high shl 4) or low).toByte()
        }
        return output
    }

    fun constantTimeEquals(left: String, right: String): Boolean {
        val a = decodeOrNull(left) ?: return false
        val b = decodeOrNull(right) ?: return false
        return MessageDigest.isEqual(a, b)
    }
}
