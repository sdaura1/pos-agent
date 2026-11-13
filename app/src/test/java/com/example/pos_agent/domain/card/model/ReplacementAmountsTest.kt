package com.example.pos_agent.domain.card.model

import com.example.pos_agent.domain.shared.Money
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReplacementAmountsTest {

    @Test
    fun `ReplacementAmounts should accept zero when built for a full reversal`() {
        // Arrange
        val zero = Money.zero()

        // Act
        val amounts = ReplacementAmounts(transactionAmount = zero)

        // Assert
        assertEquals(0L, amounts.transactionAmount.minorUnits)
    }

    @Test
    fun `ReplacementAmounts should accept corrected amounts when built`() {
        // Arrange
        val amount = Money.of(1_000)

        // Act
        val amounts = ReplacementAmounts(
            transactionAmount = amount,
            settlementAmount = amount,
            transactionFee = Money.zero(),
            settlementFee = Money.zero(),
        )

        // Assert
        assertEquals(1_000L, amounts.transactionAmount.minorUnits)
    }

    @Test
    fun `ReplacementAmounts should reject a negative amount when built`() {
        // Arrange
        val negative = Money.of(-1)

        // Act
        val failure = runCatching {
            ReplacementAmounts(transactionAmount = negative)
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun `ReplacementAmounts should reject a negative fee when built`() {
        // Arrange
        val negativeFee = Money.of(-1)

        // Act
        val failure = runCatching {
            ReplacementAmounts(
                transactionAmount = Money.zero(),
                transactionFee = negativeFee,
            )
        }.exceptionOrNull()

        // Assert
        assertTrue(failure is IllegalArgumentException)
    }
}
