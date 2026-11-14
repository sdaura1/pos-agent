package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardCaptureErrorCode
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EmvSessionPropertyTest {

    @Test
    fun `applying random event sequences should never reach an illegal state`() {
        // Arrange
        val random = Random(20261114)

        // Act
        repeat(500) {
            val session = EmvSession()
            session.start()
            var previousPendingId: Long? = null

            for (step in 0 until 50) {
                if (session.snapshot.isTerminal) break
                val before = session.snapshot
                val transition = randomEvent(session, random)
                assertInvariants(session, transition, before)

                val pending = session.snapshot.pendingPrompt
                if (pending != null && pending.id != previousPendingId) {
                    assertTrue(pending.id > (previousPendingId ?: 0L))
                    previousPendingId = pending.id
                }
            }

            // Assert
            if (session.snapshot.isTerminal) {
                assertTerminalIsStable(session)
            }
        }
    }

    private fun assertInvariants(
        session: EmvSession,
        transition: EmvTransition,
        before: EmvSessionSnapshot,
    ) {
        val after = transition.snapshot
        assertTrue(after.state in ALL_STATES)
        assertEquals(after.state in TERMINAL_STATES, after.isTerminal)
        assertEquals(after, session.snapshot)
        assertTrue(after.secondTapCount <= 2)

        if (after.isTerminal) {
            assertNotNull(after.result)
            assertNull(after.pendingPrompt)
        } else {
            assertNull(after.result)
        }

        if (before.isTerminal) {
            assertTrue(transition is EmvTransition.Rejected)
            assertEquals(
                EmvRejection.SessionFinished,
                (transition as EmvTransition.Rejected).reason,
            )
            assertEquals(before.result, after.result)
        }

        if (transition is EmvTransition.Resolved) {
            assertTrue(after.isTerminal)
            assertEquals(transition.result, after.result)
        }
    }

    private fun assertTerminalIsStable(session: EmvSession) {
        val result = session.snapshot.result
        assertNotNull(result)

        assertTrue(session.start() is EmvTransition.Rejected)
        assertTrue(session.cancel() is EmvTransition.Rejected)
        assertTrue(session.onCardRemoved() is EmvTransition.Rejected)
        assertTrue(session.onTimeout() is EmvTransition.Rejected)
        assertTrue(session.onPrompt(cardPresented()) is EmvTransition.Rejected)
        assertTrue(session.reply(0, EmvReply.Cancel) is EmvTransition.Rejected)
        assertEquals(result, session.snapshot.result)
    }

    private fun randomEvent(session: EmvSession, random: Random): EmvTransition =
        when (random.nextInt(16)) {
            in 0..5 -> session.onPrompt(randomPrompt(random))
            in 6..10 -> randomReply(session, random)
            11 -> session.onPrompt(cardPresented())
            12 -> session.onTimeout()
            13 -> session.onCardRemoved()
            14 -> session.cancel()
            else -> session.onPrompt(EmvPrompt.SecondTap("random"))
        }

    private fun randomPrompt(random: Random): EmvPrompt = when (random.nextInt(10)) {
        0 -> cardPresented()
        1 -> selectPrompt()
        2 -> confirmPrompt()
        3 -> pinPrompt()
        4 -> onlinePrompt()
        5 -> EmvPrompt.SecondTap("random")
        6 -> EmvPrompt.KernelSelected("VISA")
        7 -> EmvPrompt.Failure(CardCaptureErrorCode.UNSUPPORTED_CARD)
        8 -> finishedApproved()
        else -> finishedDeclined()
    }

    private fun randomReply(session: EmvSession, random: Random): EmvTransition {
        val pending = session.snapshot.pendingPrompt
            ?: return session.reply(random.nextLong(0, 10), EmvReply.Cancel)

        return session.reply(
            pending.id,
            when (random.nextInt(12)) {
                0 -> EmvReply.Cancel
                1 -> EmvReply.Accept
                2 -> EmvReply.Decline
                3 -> EmvReply.PinCancelled
                4 -> EmvReply.PinTimeout
                5 -> EmvReply.PinEntered(pinBlock())
                6 -> EmvReply.HostApproved()
                7 -> EmvReply.HostDeclined()
                8 -> EmvReply.HostUnknown()
                9 -> EmvReply.Select(0)
                10 -> EmvReply.Select(2)
                else -> EmvReply.Select(-1)
            },
        )
    }

    private companion object {
        val ALL_STATES = listOf(
            EmvState.Idle,
            EmvState.WaitingForCard,
            EmvState.ApplicationSelection,
            EmvState.CardholderConfirmation,
            EmvState.PinEntry,
            EmvState.Online,
            EmvState.SecondTap,
            EmvState.Completed,
            EmvState.Declined,
            EmvState.Cancelled,
            EmvState.TimedOut,
            EmvState.Failed,
        )

        val TERMINAL_STATES = listOf(
            EmvState.Completed,
            EmvState.Declined,
            EmvState.Cancelled,
            EmvState.TimedOut,
            EmvState.Failed,
        )
    }
}
