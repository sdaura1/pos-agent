package com.example.pos_agent.data.remote.nibss.iso

/**
 * Connection settings for the CTMS socket.
 *
 * The host-advertised deadline (DE62 tag 04, typically two seconds) is a
 * protocol value; [readTimeoutMillis] is the client safety margin and should be
 * at least as long as that deadline.
 */
data class NibssConnectionConfig(
    val host: String,
    val port: Int,
    val connectTimeoutMillis: Int = 5_000,
    val readTimeoutMillis: Int = 5_000,
    val maxAttempts: Int = 2,
    val initialBackoffMillis: Long = 250,
    val maxBackoffMillis: Long = 2_000,
)
