package com.example.pos_agent.domain.card.model

/**
 * Terminal location at transaction time, mandatory for NIBSS DE120.
 *
 * [capturedAtEpochMillis] lets the caller decide whether a cached fix is fresh
 * enough for the agreed no-fix policy.
 */
data class GeoCoordinates(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double? = null,
    val capturedAtEpochMillis: Long,
) {
    init {
        require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90" }
        require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180" }
        require(accuracyMeters == null || accuracyMeters >= 0.0) {
            "Accuracy cannot be negative"
        }
        require(capturedAtEpochMillis > 0) {
            "Capture timestamp is required"
        }
    }
}
