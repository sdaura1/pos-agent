package com.example.pos_agent.data.remote.nibss.iso

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class TaggedFieldCodecTest {

    @Test
    fun `encodes tag and three-digit length`() {
        val encoded = TaggedFieldCodec.encode(TaggedField("04", "02"))

        assertEquals("0400202", encoded)
    }

    @Test
    fun `encodes multiple fields in order`() {
        val encoded = TaggedFieldCodec.encode(
            TaggedField("01", "1234567890"),
            TaggedField("57", "01234567890123456789"),
        )

        assertEquals("010101234567890" + "5702001234567890123456789", encoded)
    }

    @Test
    fun `decodes a round trip`() {
        val fields = listOf(
            TaggedField("02", "11132026121500"),
            TaggedField("04", "02"),
            TaggedField("52", "MERCHANT"),
        )

        val decoded = TaggedFieldCodec.decode(TaggedFieldCodec.encode(fields))

        assertEquals(fields, decoded)
    }

    @Test
    fun `decodes an empty value`() {
        val decoded = TaggedFieldCodec.decode("04000")

        assertEquals(listOf(TaggedField("04", "")), decoded)
    }

    @Test
    fun `decodes to a map`() {
        val decoded = TaggedFieldCodec.decodeToMap("0400202" + "010101234567890")

        assertEquals(mapOf("04" to "02", "01" to "1234567890"), decoded)
    }

    @Test
    fun `empty input yields no fields`() {
        assertTrue(TaggedFieldCodec.decode("").isEmpty())
    }

    @Test
    fun `rejects a non-numeric tag on encode`() {
        assertThrows(IllegalArgumentException::class.java) {
            TaggedFieldCodec.encode(TaggedField("AB", "value"))
        }
    }

    @Test
    fun `rejects a tag that is not two characters`() {
        assertThrows(IllegalArgumentException::class.java) {
            TaggedFieldCodec.encode(TaggedField("1", "value"))
        }
    }

    @Test
    fun `rejects a value longer than 999 characters`() {
        assertThrows(IllegalArgumentException::class.java) {
            TaggedFieldCodec.encode(TaggedField("01", "x".repeat(1000)))
        }
    }

    @Test
    fun `rejects a truncated tag header`() {
        assertThrows(IllegalArgumentException::class.java) {
            TaggedFieldCodec.decode("040")
        }
    }

    @Test
    fun `rejects a non-numeric length`() {
        assertThrows(IllegalArgumentException::class.java) {
            TaggedFieldCodec.decode("04ABC02")
        }
    }

    @Test
    fun `rejects a value that runs past the end`() {
        assertThrows(IllegalArgumentException::class.java) {
            TaggedFieldCodec.decode("0400512")
        }
    }

    @Test
    fun `rejects a non-numeric tag on decode`() {
        assertThrows(IllegalArgumentException::class.java) {
            TaggedFieldCodec.decode("0A00202")
        }
    }
}
