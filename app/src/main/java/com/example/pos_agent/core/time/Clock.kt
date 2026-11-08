package com.example.pos_agent.core.time

/**
 * Abstraction over wall-clock time so use cases stay deterministic in tests.
 * Kept as epoch millis to avoid requiring java.time desugaring on minSdk 24.
 */
fun interface Clock {
    fun nowEpochMillis(): Long
}
