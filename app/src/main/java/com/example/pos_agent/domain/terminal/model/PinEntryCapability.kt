package com.example.pos_agent.domain.terminal.model

/** Whether the terminal can accept an online PIN (DE22 position 3). */
enum class PinEntryCapability {
    UNKNOWN,
    CAN_ACCEPT_PIN,
    CANNOT_ACCEPT_PIN,
}
