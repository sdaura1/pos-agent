package com.example.pos_agent.data.remote.nibss.iso

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageFramingTest {

    @Test
    fun `frame should prefix a two-byte length when given a payload`() {
        // Arrange
        val payload = "ISO".toByteArray()

        // Act
        val framed = MessageFraming.frame(payload)

        // Assert
        assertEquals(0, framed[0].toInt())
        assertEquals(3, framed[1].toInt())
        assertArrayEquals(payload, framed.copyOfRange(2, framed.size))
    }

    @Test
    fun `read should round trip a large payload when framed`() {
        // Arrange
        val payload = ByteArray(300) { (it % 26 + 'A'.code).toByte() }
        val framed = MessageFraming.frame(payload)

        // Act
        val decoded = MessageFraming.read(ByteArrayInputStream(framed))

        // Assert
        assertArrayEquals(payload, decoded)
    }

    @Test
    fun `read should handle a trickling stream when bytes arrive one by one`() {
        // Arrange
        val payload = "08002238000000800005".toByteArray()
        val framed = MessageFraming.frame(payload)
        val trickle = object : InputStream() {
            private var index = 0

            override fun read(): Int =
                if (index < framed.size) framed[index++].toInt() and 0xFF else -1
        }

        // Act
        val decoded = MessageFraming.read(trickle)

        // Assert
        assertArrayEquals(payload, decoded)
    }

    @Test
    fun `read should return null when the stream is cleanly closed`() {
        // Arrange
        val input = ByteArrayInputStream(ByteArray(0))

        // Act
        val decoded = MessageFraming.read(input)

        // Assert
        assertNull(decoded)
    }

    @Test
    fun `read should reject a truncated header when one byte is available`() {
        // Arrange
        val input = ByteArrayInputStream(byteArrayOf(0))

        // Act
        val failure = runCatching { MessageFraming.read(input) }.exceptionOrNull()

        // Assert
        assertTrue(failure is EOFException)
    }

    @Test
    fun `read should reject a truncated payload when bytes are missing`() {
        // Arrange
        val input = ByteArrayInputStream(byteArrayOf(0, 5, 1, 2))

        // Act
        val failure = runCatching { MessageFraming.read(input) }.exceptionOrNull()

        // Assert
        assertTrue(failure is EOFException)
    }

    @Test
    fun `read should reject an empty frame when the length is zero`() {
        // Arrange
        val input = ByteArrayInputStream(byteArrayOf(0, 0))

        // Act
        val failure = runCatching { MessageFraming.read(input) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IOException)
    }

    @Test
    fun `frame should reject an oversized payload when it exceeds the limit`() {
        // Arrange
        val payload = ByteArray(MessageFraming.MAX_FRAME_LENGTH + 1)

        // Act
        val failure = runCatching { MessageFraming.frame(payload) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `write should emit the same bytes as frame when given a payload`() {
        // Arrange
        val payload = "0800".toByteArray()
        val output = ByteArrayOutputStream()

        // Act
        MessageFraming.write(output, payload)

        // Assert
        assertArrayEquals(MessageFraming.frame(payload), output.toByteArray())
    }
}
