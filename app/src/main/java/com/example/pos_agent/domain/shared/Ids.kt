package com.example.pos_agent.domain.shared

/**
 * Identifiers are value objects, so they cannot be swapped by accident.
 */
@JvmInline
value class TerminalId(val value: String)

@JvmInline
value class MerchantId(val value: String)
