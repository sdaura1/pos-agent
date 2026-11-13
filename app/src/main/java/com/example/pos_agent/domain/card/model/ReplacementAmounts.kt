package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money

/**
 * Corrected amounts for a partial or full reversal (DE95).
 *
 * Separate from the original [Money] so a partial reversal cannot accidentally
 * reuse the original amount. A full reversal uses zero amounts.
 */
data class ReplacementAmounts(
    val transactionAmount: Money,
    val settlementAmount: Money? = null,
    val transactionFee: Money? = null,
    val settlementFee: Money? = null,
) {
    init {
        require(transactionAmount.minorUnits >= 0) { "Replacement amount cannot be negative" }
        require(settlementAmount == null || settlementAmount.minorUnits >= 0) {
            "Settlement amount cannot be negative"
        }
        require(transactionFee == null || transactionFee.minorUnits >= 0) {
            "Transaction fee cannot be negative"
        }
        require(settlementFee == null || settlementFee.minorUnits >= 0) {
            "Settlement fee cannot be negative"
        }
    }
}
