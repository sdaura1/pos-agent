package com.example.pos_agent.domain.card.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class GeoCoordinatesTest {

    @Test
    fun `accepts a fix inside the valid ranges`() {
        val coordinates = GeoCoordinates(
            latitude = 6.52440,
            longitude = 3.37921,
            accuracyMeters = 12.0,
            capturedAtEpochMillis = 1_700_000_000_000,
        )

        assertEquals(6.52440, coordinates.latitude, 0.000001)
    }

    @Test
    fun `accepts boundary values`() {
        GeoCoordinates(-90.0, -180.0, capturedAtEpochMillis = 1)
        GeoCoordinates(90.0, 180.0, capturedAtEpochMillis = 1)
    }

    @Test
    fun `rejects an out of range latitude`() {
        assertThrows(IllegalArgumentException::class.java) {
            GeoCoordinates(90.1, 0.0, capturedAtEpochMillis = 1)
        }
    }

    @Test
    fun `rejects an out of range longitude`() {
        assertThrows(IllegalArgumentException::class.java) {
            GeoCoordinates(0.0, 180.1, capturedAtEpochMillis = 1)
        }
    }

    @Test
    fun `rejects negative accuracy`() {
        assertThrows(IllegalArgumentException::class.java) {
            GeoCoordinates(6.5, 3.3, accuracyMeters = -1.0, capturedAtEpochMillis = 1)
        }
    }

    @Test
    fun `rejects a missing capture timestamp`() {
        assertThrows(IllegalArgumentException::class.java) {
            GeoCoordinates(6.5, 3.3, capturedAtEpochMillis = 0)
        }
    }
}
