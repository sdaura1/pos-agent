package com.example.pos_agent.data.remote.nibss.iso

import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * CTMS framing: a 2-byte binary big-endian length followed by the ISO 8583
 * application data. There is no TPDU in the specification.
 */
object MessageFraming {

    /** A 2-byte unsigned length field can address at most 65,535 bytes. */
    const val MAX_FRAME_LENGTH: Int = 0xFFFF

    fun frame(payload: ByteArray): ByteArray {
        require(payload.isNotEmpty()) { "Cannot frame an empty payload" }
        require(payload.size <= MAX_FRAME_LENGTH) {
            "Payload of ${payload.size} bytes exceeds the 2-byte length header"
        }
        val framed = ByteArray(payload.size + 2)
        framed[0] = ((payload.size ushr 8) and 0xFF).toByte()
        framed[1] = (payload.size and 0xFF).toByte()
        payload.copyInto(framed, destinationOffset = 2)
        return framed
    }

    fun write(output: OutputStream, payload: ByteArray) {
        output.write(frame(payload))
    }

    /** Returns `null` on a clean EOF before the next frame starts. */
    fun read(input: InputStream): ByteArray? {
        val high = input.read()
        if (high == -1) return null

        val low = input.read()
        if (low == -1) throw EOFException("Truncated 2-byte length header")

        val length = (high shl 8) or low
        if (length == 0) throw IOException("Empty frame")

        val payload = ByteArray(length)
        var offset = 0
        while (offset < length) {
            val read = input.read(payload, offset, length - offset)
            if (read == -1) {
                throw EOFException("Truncated frame: $offset of $length bytes read")
            }
            offset += read
        }
        return payload
    }
}
