package com.example.pos_agent.data.remote.nibss.iso

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Iso8583CodecTest {

    private val codec = Iso8583Codec()

    @Test
    fun `newMessage carries the MTI and packager`() {
        val message = codec.newMessage("0800").apply { set(3, "9B0000") }

        assertEquals("0800", message.mti)
        assertEquals("0800", codec.unpack(codec.pack(message)).mti)
    }

    @Test
    fun `packs a network management request in the CTMS ascii format`() {
        val packed = codec.pack(networkRequest())

        assertEquals(expectedNetworkRequest(), packed.toString(Charsets.US_ASCII))
    }

    @Test
    fun `round trips every defined field`() {
        val message = networkRequest()

        val decoded = codec.unpack(codec.pack(message))

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
    fun `writes and reads a framed message`() {
        val out = ByteArrayOutputStream()

        codec.write(out, networkRequest())
        val decoded = codec.read(ByteArrayInputStream(out.toByteArray()))

        assertEquals("0800", decoded.mti)
        assertEquals("000001", decoded.getString(11))
    }

    @Test
    fun `packs the secondary bitmap for DE120 and DE128`() {
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

        val packed = codec.pack(message).toString(Charsets.US_ASCII)
        val bitmap = packed.substring(4, 4 + 32)

        assertEquals(32, bitmap.length)
        assertTrue("Secondary bitmap should set DE120 and DE128: $bitmap", bitmap.substring(16).endsWith("0101"))

        val decoded = codec.unpack(packed.toByteArray(Charsets.US_ASCII))
        assertEquals(coordinates, decoded.getString(120))
        assertEquals("A".repeat(64), decoded.getString(128))
    }

    @Test
    fun `keeps pin block and security data as ascii hex`() {
        val pinBlock = "0123456789ABCDEF"
        val securityInfo = "A1B2".repeat(24)
        val message = codec.newMessage("0200").apply {
            set(3, "000000")
            set(52, pinBlock)
            set(53, securityInfo)
        }

        val decoded = codec.unpack(codec.pack(message))

        assertEquals(pinBlock, decoded.getString(52))
        assertEquals(securityInfo, decoded.getString(53))
    }

    private fun networkRequest() = codec.newMessage("0800").apply {
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
