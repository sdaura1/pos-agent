package com.example.pos_agent.data.remote.nibss.iso

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class MessageFramingTest {

    @Test
    fun `prefixes the payload with a big-endian two-byte length`() {
        val framed = MessageFraming.frame("ISO".toByteArray())

        assertEquals(0, framed[0].toInt())
        assertEquals(3, framed[1].toInt())
        assertArrayEquals("ISO".toByteArray(), framed.copyOfRange(2, framed.size))
    }

    @Test
    fun `round trips a payload larger than 255 bytes`() {
        val payload = ByteArray(300) { (it % 26 + 'A'.code).toByte() }

        val framed = MessageFraming.frame(payload)
        val decoded = MessageFraming.read(ByteArrayInputStream(framed))

        assertArrayEquals(payload, decoded)
    }

    @Test
    fun `reads correctly when the stream returns one byte at a time`() {
        val payload = "08002238000000800005".toByteArray()
        val framed = MessageFraming.frame(payload)
        val trickle = object : InputStream() {
            private var index = 0
            override fun read(): Int =
                if (index < framed.size) framed[index++].toInt() and 0xFF else -1
        }

        assertArrayEquals(payload, MessageFraming.read(trickle))
    }

    @Test
    fun `returns null on a clean end of stream`() {
        assertNull(MessageFraming.read(ByteArrayInputStream(ByteArray(0))))
    }

    @Test
    fun `rejects a truncated length header`() {
        assertThrows(EOFException::class.java) {
            MessageFraming.read(ByteArrayInputStream(byteArrayOf(0)))
        }
    }

    @Test
    fun `rejects a truncated payload`() {
        assertThrows(EOFException::class.java) {
            MessageFraming.read(ByteArrayInputStream(byteArrayOf(0, 5, 1, 2)))
        }
    }

    @Test
    fun `rejects an empty frame`() {
        assertThrows(IOException::class.java) {
            MessageFraming.read(ByteArrayInputStream(byteArrayOf(0, 0)))
        }
    }

    @Test
    fun `rejects a payload larger than the length header can express`() {
        assertThrows(IllegalArgumentException::class.java) {
            MessageFraming.frame(ByteArray(MessageFraming.MAX_FRAME_LENGTH + 1))
        }
    }

    @Test
    fun `write emits the same bytes as frame`() {
        val payload = "0800".toByteArray()
        val out = ByteArrayOutputStream()

        MessageFraming.write(out, payload)

        assertArrayEquals(MessageFraming.frame(payload), out.toByteArray())
    }
}
