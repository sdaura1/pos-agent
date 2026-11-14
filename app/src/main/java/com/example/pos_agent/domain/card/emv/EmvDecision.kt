package com.example.pos_agent.domain.card.emv

/**
 * Final decision the kernel reports with [EmvPrompt.Finished].
 *
 * The host result is not repeated here: the caller already knows it because it
 * answered [EmvPrompt.RequestOnline]. This value only says what the kernel
 * decided after consuming that answer and any issuer scripts.
 */
enum class EmvDecision {
    APPROVED,
    DECLINED,
}
