package com.example.pos_agent.domain.terminal.model

import com.example.pos_agent.domain.shared.MerchantId
import com.example.pos_agent.domain.shared.TerminalId

/** A registered card terminal belonging to a merchant. */
data class Terminal(
    val id: TerminalId,
    val merchantId: MerchantId,
    val serialNumber: String,
    val active: Boolean = true,
)
