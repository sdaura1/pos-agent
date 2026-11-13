package com.example.pos_agent.data.remote.nibss.iso

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaggedFieldCodecTest {

    @Test
    fun `encode should produce tag and length when given a field`() {
        // Arrange
        val field = TaggedField("04", "02")

        // Act
        val encoded = TaggedFieldCodec.encode(field)

        // Assert
        assertEquals("0400202", encoded)
    }

    @Test
    fun `encode should keep field order when given multiple fields`() {
        // Arrange
        val first = TaggedField("01", "1234567890")
        val second = TaggedField("57", "01234567890123456789")

        // Act
        val encoded = TaggedFieldCodec.encode(first, second)

        // Assert
        assertEquals("010101234567890" + "5702001234567890123456789", encoded)
    }

    @Test
    fun `decode should round trip fields when encoded`() {
        // Arrange
        val fields = listOf(
            TaggedField("02", "11132026121500"),
            TaggedField("04", "02"),
            TaggedField("52", "MERCHANT"),
        )
        val encoded = TaggedFieldCodec.encode(fields)

        // Act
        val decoded = TaggedFieldCodec.decode(encoded)

        // Assert
        assertEquals(fields, decoded)
    }

    @Test
    fun `decode should preserve an empty value when the length is zero`() {
        // Arrange
        val encoded = "04000"

        // Act
        val decoded = TaggedFieldCodec.decode(encoded)

        // Assert
        assertEquals(listOf(TaggedField("04", "")), decoded)
    }

    @Test
    fun `decodeToMap should map tags to values when fields are decoded`() {
        // Arrange
        val encoded = "0400202" + "010101234567890"

        // Act
        val decoded = TaggedFieldCodec.decodeToMap(encoded)

        // Assert
        assertEquals(mapOf("04" to "02", "01" to "1234567890"), decoded)
    }

    @Test
    fun `decode should return an empty list when the input is empty`() {
        // Arrange
        val encoded = ""

        // Act
        val decoded = TaggedFieldCodec.decode(encoded)

        // Assert
        assertTrue(decoded.isEmpty())
    }

    @Test
    fun `encode should reject a non-numeric tag when called`() {
        // Arrange
        val field = TaggedField("AB", "value")

        // Act
        val failure = runCatching { TaggedFieldCodec.encode(field) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `encode should reject a tag that is not two characters when called`() {
        // Arrange
        val field = TaggedField("1", "value")

        // Act
        val failure = runCatching { TaggedFieldCodec.encode(field) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `encode should reject a value longer than 999 characters when called`() {
        // Arrange
        val field = TaggedField("01", "x".repeat(1000))

        // Act
        val failure = runCatching { TaggedFieldCodec.encode(field) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `decode should reject a truncated tag header when called`() {
        // Arrange
        val encoded = "040"

        // Act
        val failure = runCatching { TaggedFieldCodec.decode(encoded) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `decode should reject a non-numeric length when called`() {
        // Arrange
        val encoded = "04ABC02"

        // Act
        val failure = runCatching { TaggedFieldCodec.decode(encoded) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `decode should reject a value that runs past the end when called`() {
        // Arrange
        val encoded = "0400512"

        // Act
        val failure = runCatching { TaggedFieldCodec.decode(encoded) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `decode should reject a non-numeric tag when called`() {
        // Arrange
        val encoded = "0A00202"

        // Act
        val failure = runCatching { TaggedFieldCodec.decode(encoded) }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }
}
