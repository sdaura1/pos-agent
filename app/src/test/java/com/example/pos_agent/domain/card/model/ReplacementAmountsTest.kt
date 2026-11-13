package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ReplacementAmountsTest {

    @Test
    fun `accepts a zero replacement for a full reversal`() {
        val amounts = ReplacementAmounts(transactionAmount = Money.zero())

        assertEquals(0L, amounts.transactionAmount.minorUnits)
    }

    @Test
    fun `accepts corrected amounts and fees`() {
        val amounts = ReplacementAmounts(
            transactionAmount = Money.of(1_000),
            settlementAmount = Money.of(1_000),
            transactionFee = Money.zero(),
            settlementFee = Money.zero(),
        )

        assertEquals(1_000L, amounts.transactionAmount.minorUnits)
    }

    @Test
    fun `rejects a negative transaction amount`() {
        assertThrows(IllegalArgumentException::class.java) {
            ReplacementAmounts(transactionAmount = Money.of(-1))
        }
    }

    @Test
    fun `rejects a negative fee`() {
        assertThrows(IllegalArgumentException::class.java) {
            ReplacementAmounts(
                transactionAmount = Money.zero(),
                transactionFee = Money.of(-1),
            )
        }
    }
}
