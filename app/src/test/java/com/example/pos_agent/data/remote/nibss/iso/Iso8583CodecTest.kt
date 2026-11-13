package com.example.pos_agent.data.remote.nibss.iso

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Iso8583CodecTest {

    private val codec = Iso8583Codec()

    @Test
    fun `newMessage should carry the MTI and packager when created`() {
        // Arrange
        val message = codec.newMessage("0800").apply { set(3, "9B0000") }

        // Act
        val decoded = codec.unpack(codec.pack(message))

        // Assert
        assertEquals("0800", message.mti)
        assertEquals("0800", decoded.mti)
    }

    @Test
    fun `pack should produce the CTMS ASCII layout when given a request`() {
        // Arrange
        val request = networkRequest()

        // Act
        val packed = codec.pack(request)

        // Assert
        assertEquals(expectedNetworkRequest(), packed.toString(Charsets.US_ASCII))
    }

    @Test
    fun `unpack should restore every field when given a packed message`() {
        // Arrange
        val request = networkRequest()
        val packed = codec.pack(request)

        // Act
        val decoded = codec.unpack(packed)

        // Assert
        assertEquals("0800", decoded.mti)
        assertEquals("9B0000", decoded.getString(3))
        assertEquals("1113221500", decoded.getString(7))
        assertEquals("000001", decoded.getString(11))
        assertEquals("221500", decoded.getString(12))
        assertEquals("1113", decoded.getString(13))
        assertEquals("TID00001", decoded.getString(41))
        assertEquals("010101234567890", decoded.getString(62))
        assertEquals("H".repeat(64), decoded.getString(64))
    }

    @Test
    fun `write and read should round trip a message when used together`() {
        // Arrange
        val output = ByteArrayOutputStream()

        // Act
        codec.write(output, networkRequest())
        val decoded = codec.read(ByteArrayInputStream(output.toByteArray()))

        // Assert
        assertEquals("0800", decoded.mti)
        assertEquals("000001", decoded.getString(11))
    }

    @Test
    fun `pack should set the secondary bitmap when DE120 and DE128 exist`() {
        // Arrange
        val coordinates = TaggedFieldCodec.encode(
            TaggedField("01", "06.52440"),
            TaggedField("02", "003.37921"),
        )
        val message = codec.newMessage("0200").apply {
            set(3, "000000")
            set(4, "000000001000")
            set(11, "000001")
            set(41, "TID00001")
            set(120, coordinates)
            set(128, "A".repeat(64))
        }

        // Act
        val packed = codec.pack(message).toString(Charsets.US_ASCII)
        val bitmap = packed.substring(4, 4 + 32)

        // Assert
        assertEquals(32, bitmap.length)
        assertTrue(
            "Secondary bitmap should set DE120 and DE128: $bitmap",
            bitmap.substring(16).endsWith("0101"),
        )

        val decoded = codec.unpack(packed.toByteArray(Charsets.US_ASCII))
        assertEquals(coordinates, decoded.getString(120))
        assertEquals("A".repeat(64), decoded.getString(128))
    }

    @Test
    fun `pack should keep PIN and security data as ASCII hex when given`() {
        // Arrange
        val pinBlock = "0123456789ABCDEF"
        val securityInfo = "A1B2".repeat(24)
        val message = codec.newMessage("0200").apply {
            set(3, "000000")
            set(52, pinBlock)
            set(53, securityInfo)
        }

        // Act
        val decoded = codec.unpack(codec.pack(message))

        // Assert
        assertEquals(pinBlock, decoded.getString(52))
        assertEquals(securityInfo, decoded.getString(53))
    }

    private fun networkRequest(): org.jpos.iso.ISOMsg =
        codec.newMessage("0800").apply {
            set(3, "9B0000")
            set(7, "1113221500")
            set(11, "000001")
            set(12, "221500")
            set(13, "1113")
            set(41, "TID00001")
            set(62, TaggedFieldCodec.encode(TaggedField("01", "1234567890")))
            set(64, "H".repeat(64))
        }

    /**
     * Golden value for the fixture above. The bitmap covers DE3, DE7, DE11,
     * DE12, DE13, DE41, DE62 and DE64.
     */
    private fun expectedNetworkRequest(): String = buildString {
        append("0800")
        append("2238000000800005")
        append("9B0000")
        append("1113221500")
        append("000001")
        append("221500")
        append("1113")
        append("TID00001")
        append("015") // DE62 length
        append(TaggedFieldCodec.encode(TaggedField("01", "1234567890")))
        append("H".repeat(64))
    }
}
