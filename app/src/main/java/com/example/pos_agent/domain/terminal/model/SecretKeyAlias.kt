package com.example.pos_agent.domain.terminal.model

/**
 * Logical names for terminal key material.
 *
 * Not every alias has to be stored by the app: the acquirer or the Kozen
 * security module may own TMK/TPK/IPEK. The app must at minimum hold the
 * terminal session key (TSK), because DE128 is computed in the app.
 */
enum class SecretKeyAlias {
    TERMINAL_MASTER_KEY,
    TERMINAL_PIN_KEY,
    TERMINAL_SESSION_KEY,
    IPEK_TRACK2,
    IPEK_EMV,
}
