package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeoCoordinatesTest {

    @Test
    fun `GeoCoordinates should accept a valid fix when built`() {
        // Arrange
        val latitude = 6.52440
        val longitude = 3.37921

        // Act
        val coordinates = GeoCoordinates(
            latitude = latitude,
            longitude = longitude,
            accuracyMeters = 12.0,
            capturedAtEpochMillis = 1_700_000_000_000,
        )

        // Assert
        assertEquals(latitude, coordinates.latitude, 0.000001)
    }

    @Test
    fun `GeoCoordinates should accept the boundary values when built`() {
        // Arrange
        val minimum = GeoCoordinates(-90.0, -180.0, capturedAtEpochMillis = 1)
        val maximum = GeoCoordinates(90.0, 180.0, capturedAtEpochMillis = 1)

        // Act
        val minLatitude = minimum.latitude
        val maxLatitude = maximum.latitude

        // Assert
        assertEquals(-90.0, minLatitude, 0.0)
        assertEquals(90.0, maxLatitude, 0.0)
    }

    @Test
    fun `GeoCoordinates should reject an out-of-range latitude when built`() {
        // Arrange
        val invalidLatitude = 90.1

        // Act
        val failure = runCatching {
            GeoCoordinates(invalidLatitude, 0.0, capturedAtEpochMillis = 1)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `GeoCoordinates should reject an out-of-range longitude when built`() {
        // Arrange
        val invalidLongitude = 180.1

        // Act
        val failure = runCatching {
            GeoCoordinates(0.0, invalidLongitude, capturedAtEpochMillis = 1)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `GeoCoordinates should reject negative accuracy when built`() {
        // Arrange
        val negativeAccuracy = -1.0

        // Act
        val failure = runCatching {
            GeoCoordinates(
                latitude = 6.5,
                longitude = 3.3,
                accuracyMeters = negativeAccuracy,
                capturedAtEpochMillis = 1,
            )
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `GeoCoordinates should reject a missing timestamp when built`() {
        // Arrange
        val missingTimestamp = 0L

        // Act
        val failure = runCatching {
            GeoCoordinates(6.5, 3.3, capturedAtEpochMillis = missingTimestamp)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }
}
