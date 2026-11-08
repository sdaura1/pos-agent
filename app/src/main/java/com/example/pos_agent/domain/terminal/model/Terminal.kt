package com.example.pos_agent.domain.terminal.model

/** A registered card terminal belonging to a merchant. */
data class Terminal(
    val id: String,
    val merchantId: String,
    val serialNumber: String,
    val active: Boolean = true,
)
