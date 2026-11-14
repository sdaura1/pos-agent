package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardCaptureErrorCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EmvSessionTest {

    @Test
    fun `start should move to waiting for card when the session is idle`() {
        // Arrange
        val session = EmvSession()

        // Act
        val transition = session.start()

        // Assert
        assertTrue(transition is EmvTransition.Accepted)
        assertEquals(EmvState.WaitingForCard, session.snapshot.state)
    }

    @Test
    fun `start should reject a second start when already started`() {
        // Arrange
        val session = EmvSession()
        session.start()

        // Act
        val transition = session.start()

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionAlreadyStarted,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `start should reject a restart when the session is terminal`() {
        // Arrange
        val session = EmvSession()
        session.start()
        session.cancel()

        // Act
        val transition = session.start()

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionFinished,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `onPrompt should reject a prompt when the session has not started`() {
        // Arrange
        val session = EmvSession()

        // Act
        val transition = session.onPrompt(cardPresented())

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionNotStarted,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `onPrompt should accept a card presentation when waiting for card`() {
        // Arrange
        val session = started()

        // Act
        val transition = session.onPrompt(cardPresented())

        // Assert
        assertTrue(transition is EmvTransition.Accepted)
        assertTrue(session.snapshot.cardPresented)
    }

    @Test
    fun `onPrompt should reject a dialogue prompt before the card is presented`() {
        // Arrange
        val session = started()

        // Act
        val transition = session.onPrompt(selectPrompt())

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertTrue(
            (transition as EmvTransition.Rejected).reason
                is EmvRejection.CardNotPresented,
        )
    }

    @Test
    fun `onPrompt should move to application selection when applications match`() {
        // Arrange
        val session = presented()

        // Act
        val transition = session.onPrompt(selectPrompt())

        // Assert
        assertTrue(transition is EmvTransition.AwaitingReply)
        assertEquals(EmvState.ApplicationSelection, session.snapshot.state)
        assertEquals(selectPrompt(), session.snapshot.pendingPrompt?.prompt)
    }

    @Test
    fun `onPrompt should move to confirmation when card info needs confirming`() {
        // Arrange
        val session = presented()

        // Act
        session.onPrompt(confirmPrompt())

        // Assert
        assertEquals(EmvState.CardholderConfirmation, session.snapshot.state)
    }

    @Test
    fun `onPrompt should move to pin entry when a pin is requested`() {
        // Arrange
        val session = presented()

        // Act
        session.onPrompt(pinPrompt())

        // Assert
        assertEquals(EmvState.PinEntry, session.snapshot.state)
    }

    @Test
    fun `onPrompt should move online when the host request is ready`() {
        // Arrange
        val session = presented()

        // Act
        session.onPrompt(onlinePrompt())

        // Assert
        assertEquals(EmvState.Online, session.snapshot.state)
    }

    @Test
    fun `onPrompt should move to second tap when the kernel asks for a re-tap`() {
        // Arrange
        val session = presented()

        // Act
        session.onPrompt(EmvPrompt.SecondTap("card removed too soon"))

        // Assert
        assertEquals(EmvState.SecondTap, session.snapshot.state)
        assertEquals(1, session.snapshot.secondTapCount)
        assertFalse(session.snapshot.cardPresented)
    }

    @Test
    fun `onPrompt should finish approved when the kernel approves`() {
        // Arrange
        val session = presented()

        // Act
        val resolved = session.onPrompt(finishedApproved()).resolved()

        // Assert
        assertEquals(EmvState.Completed, session.snapshot.state)
        assertEquals(AcquisitionResult.Approved(capture()), resolved.result)
    }

    @Test
    fun `onPrompt should finish declined when the kernel declines`() {
        // Arrange
        val session = presented()

        // Act
        val resolved = session.onPrompt(finishedDeclined()).resolved()

        // Assert
        assertEquals(EmvState.Declined, session.snapshot.state)
        assertEquals(AcquisitionResult.Declined(capture()), resolved.result)
    }

    @Test
    fun `onPrompt should finish failed when the kernel fails`() {
        // Arrange
        val session = presented()

        // Act
        val resolved = session.onPrompt(
            EmvPrompt.Failure(CardCaptureErrorCode.UNSUPPORTED_CARD, "not supported"),
        ).resolved()

        // Assert
        assertEquals(EmvState.Failed, session.snapshot.state)
        assertEquals(
            AcquisitionResult.Failed(
                CardCaptureErrorCode.UNSUPPORTED_CARD,
                "not supported",
            ),
            resolved.result,
        )
    }

    @Test
    fun `onPrompt should reject finished when the card was not presented`() {
        // Arrange
        val session = started()

        // Act
        val transition = session.onPrompt(finishedApproved())

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertTrue(
            (transition as EmvTransition.Rejected).reason
                is EmvRejection.CardNotPresented,
        )
    }

    @Test
    fun `onPrompt should reject a prompt when another prompt is pending`() {
        // Arrange
        val session = presented()
        session.onPrompt(selectPrompt())

        // Act
        val transition = session.onPrompt(confirmPrompt())

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertTrue(
            (transition as EmvTransition.Rejected).reason
                is EmvRejection.PromptAlreadyPending,
        )
    }

    @Test
    fun `onPrompt should reject a repeated card presentation when card is present`() {
        // Arrange
        val session = presented()

        // Act
        val transition = session.onPrompt(cardPresented())

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertTrue(
            (transition as EmvTransition.Rejected).reason
                is EmvRejection.UnexpectedPrompt,
        )
    }

    @Test
    fun `onPrompt should reject a prompt when the session is terminal`() {
        // Arrange
        val session = presented()
        session.onPrompt(finishedApproved())

        // Act
        val transition = session.onPrompt(cardPresented())

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionFinished,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `reply should reject a reply when no prompt is pending`() {
        // Arrange
        val session = presented()

        // Act
        val transition = session.reply(99, EmvReply.Cancel)

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.NoPendingPrompt,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `reply should reject a stale reply when the prompt was already answered`() {
        // Arrange
        val session = presented()
        val select = session.onPrompt(selectPrompt()).awaiting()
        session.reply(select.prompt.id, EmvReply.Select(0))

        // Act
        val transition = session.reply(select.prompt.id, EmvReply.Select(0))

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.StalePromptReply(select.prompt.id),
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `reply should reject a stale reply when a newer prompt is pending`() {
        // Arrange
        val session = presented()
        val select = session.onPrompt(selectPrompt()).awaiting()
        session.reply(select.prompt.id, EmvReply.Select(0))
        val confirm = session.onPrompt(confirmPrompt()).awaiting()

        // Act
        val transition = session.reply(select.prompt.id, EmvReply.Accept)

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(confirm.prompt, session.snapshot.pendingPrompt)
    }

    @Test
    fun `reply should reject a select index when it is out of range`() {
        // Arrange
        val session = presented()
        val select = session.onPrompt(selectPrompt()).awaiting()

        // Act
        val transition = session.reply(select.prompt.id, EmvReply.Select(5))

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertNotNull(session.snapshot.pendingPrompt)
    }

    @Test
    fun `reply should reject a reply type when it does not match the prompt`() {
        // Arrange
        val session = presented()
        val select = session.onPrompt(selectPrompt()).awaiting()

        // Act
        val transition = session.reply(select.prompt.id, EmvReply.Accept)

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertTrue(
            (transition as EmvTransition.Rejected).reason
                is EmvRejection.DisallowedReply,
        )
    }

    @Test
    fun `reply should keep the session alive when the pin pad cancels`() {
        // Arrange
        val session = presented()
        val pin = session.onPrompt(pinPrompt()).awaiting()

        // Act
        val transition = session.reply(pin.prompt.id, EmvReply.PinCancelled)

        // Assert
        assertTrue(transition is EmvTransition.Accepted)
        assertEquals(EmvState.PinEntry, session.snapshot.state)
        assertNull(session.snapshot.pendingPrompt)
    }

    @Test
    fun `reply should finish timed out when the pin pad reports a timeout`() {
        // Arrange
        val session = presented()
        val pin = session.onPrompt(pinPrompt()).awaiting()

        // Act
        val resolved = session.reply(pin.prompt.id, EmvReply.PinTimeout).resolved()

        // Assert
        assertEquals(
            AcquisitionResult.TimedOut(AcquisitionTimeout.PIN),
            resolved.result,
        )
        assertEquals(pin.prompt, resolved.answeredPrompt)
        assertEquals(EmvReply.PinTimeout, resolved.reply)
    }

    @Test
    fun `reply should finish cancelled when the app cancels a pending prompt`() {
        // Arrange
        val session = presented()
        val online = session.onPrompt(onlinePrompt()).awaiting()

        // Act
        val resolved = session.reply(online.prompt.id, EmvReply.Cancel).resolved()

        // Assert
        assertEquals(AcquisitionResult.Cancelled, resolved.result)
        assertEquals(online.prompt, resolved.answeredPrompt)
        assertEquals(EmvReply.Cancel, resolved.reply)
    }

    @Test
    fun `cancel should answer the pending prompt when one is waiting`() {
        // Arrange
        val session = presented()
        val pin = session.onPrompt(pinPrompt()).awaiting()

        // Act
        val resolved = session.cancel().resolved()

        // Assert
        assertEquals(AcquisitionResult.Cancelled, resolved.result)
        assertEquals(pin.prompt, resolved.answeredPrompt)
        assertEquals(EmvReply.Cancel, resolved.reply)
        assertNull(session.snapshot.pendingPrompt)
    }

    @Test
    fun `cancel should finish cancelled when waiting for a card`() {
        // Arrange
        val session = started()

        // Act
        val resolved = session.cancel().resolved()

        // Assert
        assertEquals(EmvState.Cancelled, session.snapshot.state)
        assertEquals(AcquisitionResult.Cancelled, resolved.result)
        assertNull(resolved.answeredPrompt)
    }

    @Test
    fun `cancel should end terminal with no capture in every active state`() {
        // Arrange
        val states = listOf(
            EmvState.Idle,
            EmvState.WaitingForCard,
            EmvState.ApplicationSelection,
            EmvState.CardholderConfirmation,
            EmvState.PinEntry,
            EmvState.Online,
            EmvState.SecondTap,
        )

        states.forEach { state ->
            val session = sessionIn(state)
            val pending = session.snapshot.pendingPrompt

            // Act
            val resolved = session.cancel().resolved()

            // Assert
            assertEquals(EmvState.Cancelled, resolved.snapshot.state)
            assertEquals(AcquisitionResult.Cancelled, resolved.result)
            assertNull(resolved.snapshot.pendingPrompt)
            assertEquals(pending, resolved.answeredPrompt)
            assertEquals(
                if (pending == null) null else EmvReply.Cancel,
                resolved.reply,
            )
        }
    }

    @Test
    fun `cancel should be rejected in every terminal state`() {
        // Arrange
        val states = listOf(
            EmvState.Completed,
            EmvState.Declined,
            EmvState.Cancelled,
            EmvState.TimedOut,
            EmvState.Failed,
        )

        states.forEach { state ->
            val session = sessionIn(state)

            // Act
            val transition = session.cancel()

            // Assert
            assertTrue(transition is EmvTransition.Rejected)
            assertEquals(
                EmvRejection.SessionFinished,
                (transition as EmvTransition.Rejected).reason,
            )
        }
    }

    @Test
    fun `onTimeout should reject a timeout when the session has not started`() {
        // Arrange
        val session = EmvSession()

        // Act
        val transition = session.onTimeout()

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionNotStarted,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `onCardRemoved should reject a removal when the session has not started`() {
        // Arrange
        val session = EmvSession()

        // Act
        val transition = session.onCardRemoved()

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionNotStarted,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `reply should reject a reply when the session has not started`() {
        // Arrange
        val session = EmvSession()

        // Act
        val transition = session.reply(1, EmvReply.Cancel)

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionNotStarted,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `onCardRemoved should finish failed before the kernel finishes`() {
        // Arrange
        val session = presented()

        // Act
        val resolved = session.onCardRemoved().resolved()

        // Assert
        assertEquals(EmvState.Failed, session.snapshot.state)
        assertEquals(
            AcquisitionResult.Failed(CardCaptureErrorCode.CARD_REMOVED),
            resolved.result,
        )
    }

    @Test
    fun `onCardRemoved should answer the pending prompt with cancel`() {
        // Arrange
        val session = presented()
        val pin = session.onPrompt(pinPrompt()).awaiting()

        // Act
        val resolved = session.onCardRemoved().resolved()

        // Assert
        assertEquals(
            AcquisitionResult.Failed(CardCaptureErrorCode.CARD_REMOVED),
            resolved.result,
        )
        assertEquals(pin.prompt, resolved.answeredPrompt)
        assertEquals(EmvReply.Cancel, resolved.reply)
    }

    @Test
    fun `onCardRemoved should be rejected when the session is terminal`() {
        // Arrange
        val session = sessionIn(EmvState.Completed)

        // Act
        val transition = session.onCardRemoved()

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.SessionFinished,
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `onTimeout should finish timed out when waiting for a card`() {
        // Arrange
        val session = started()

        // Act
        val resolved = session.onTimeout().resolved()

        // Assert
        assertEquals(
            AcquisitionResult.TimedOut(AcquisitionTimeout.CARD_WAIT),
            resolved.result,
        )
    }

    @Test
    fun `onTimeout should finish timed out when waiting for the second tap`() {
        // Arrange
        val session = presented()
        session.onPrompt(EmvPrompt.SecondTap("retry"))

        // Act
        val resolved = session.onTimeout().resolved()

        // Assert
        assertEquals(
            AcquisitionResult.TimedOut(AcquisitionTimeout.SECOND_TAP),
            resolved.result,
        )
    }

    @Test
    fun `onTimeout should reject a timeout when online`() {
        // Arrange
        val session = presented()
        session.onPrompt(onlinePrompt())

        // Act
        val transition = session.onTimeout()

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertEquals(
            EmvRejection.UnexpectedTimeout(EmvState.Online),
            (transition as EmvTransition.Rejected).reason,
        )
    }

    @Test
    fun `onPrompt should accept kernel selected when the card is presented`() {
        // Arrange
        val session = presented()

        // Act
        val transition = session.onPrompt(EmvPrompt.KernelSelected("VISA"))

        // Assert
        assertTrue(transition is EmvTransition.Accepted)
        assertEquals(EmvState.WaitingForCard, session.snapshot.state)
        assertNull(session.snapshot.pendingPrompt)
    }

    @Test
    fun `onPrompt should reject a second tap when the card is absent`() {
        // Arrange
        val session = started()

        // Act
        val transition = session.onPrompt(EmvPrompt.SecondTap("retry"))

        // Assert
        assertTrue(transition is EmvTransition.Rejected)
        assertTrue(
            (transition as EmvTransition.Rejected).reason
                is EmvRejection.CardNotPresented,
        )
    }

    @Test
    fun `onPrompt should finish failed before the card is presented`() {
        // Arrange
        val session = started()

        // Act
        val resolved = session.onPrompt(
            EmvPrompt.Failure(CardCaptureErrorCode.TERMINAL_UNAVAILABLE),
        ).resolved()

        // Assert
        assertEquals(EmvState.Failed, session.snapshot.state)
        assertEquals(
            AcquisitionResult.Failed(CardCaptureErrorCode.TERMINAL_UNAVAILABLE),
            resolved.result,
        )
    }

    @Test
    fun `onPrompt should reject a third second tap when the limit is two`() {
        // Arrange
        val session = presented()
        session.onPrompt(EmvPrompt.SecondTap("first"))
        session.onPrompt(cardPresented())
        session.onPrompt(EmvPrompt.SecondTap("second"))
        session.onPrompt(cardPresented())

        // Act
        val resolved = session.onPrompt(EmvPrompt.SecondTap("third")).resolved()

        // Assert
        assertEquals(EmvState.Failed, session.snapshot.state)
        assertEquals(
            CardCaptureErrorCode.CARD_REMOVED,
            (resolved.result as AcquisitionResult.Failed).code,
        )
    }

    @Test
    fun `reply should keep the session online when the host is unknown`() {
        // Arrange
        val session = presented()
        val online = session.onPrompt(onlinePrompt()).awaiting()

        // Act
        val transition = session.reply(online.prompt.id, EmvReply.HostUnknown())

        // Assert
        assertTrue(transition is EmvTransition.Accepted)
        assertEquals(EmvState.Online, session.snapshot.state)
        assertNull(session.snapshot.pendingPrompt)
    }

    @Test
    fun `acquire should produce an approved capture when the contact flow completes`() {
        // Arrange
        val session = presented()

        // Act
        val select = session.onPrompt(selectPrompt()).awaiting()
        session.reply(select.prompt.id, EmvReply.Select(0))
        val confirm = session.onPrompt(confirmPrompt()).awaiting()
        session.reply(confirm.prompt.id, EmvReply.Accept)
        val pin = session.onPrompt(pinPrompt()).awaiting()
        session.reply(pin.prompt.id, EmvReply.PinEntered(pinBlock()))
        val online = session.onPrompt(onlinePrompt()).awaiting()
        session.reply(online.prompt.id, EmvReply.HostApproved())
        val resolved = session.onPrompt(finishedApproved()).resolved()

        // Assert
        assertEquals(AcquisitionResult.Approved(capture()), resolved.result)
        assertEquals(EmvState.Completed, session.snapshot.state)
    }

    @Test
    fun `acquire should produce a declined capture when the host declines`() {
        // Arrange
        val session = presented()
        val online = session.onPrompt(onlinePrompt()).awaiting()
        session.reply(online.prompt.id, EmvReply.HostDeclined())

        // Act
        val resolved = session.onPrompt(finishedDeclined()).resolved()

        // Assert
        assertEquals(AcquisitionResult.Declined(capture()), resolved.result)
        assertEquals(EmvState.Declined, session.snapshot.state)
    }

    private fun started(): EmvSession = EmvSession().apply { start() }

    private fun presented(): EmvSession = started().apply {
        onPrompt(cardPresented())
    }

    private fun sessionIn(state: EmvState): EmvSession {
        val session = EmvSession()
        when (state) {
            EmvState.Idle -> Unit

            EmvState.WaitingForCard -> session.start()

            EmvState.ApplicationSelection -> {
                session.start()
                session.onPrompt(cardPresented())
                session.onPrompt(selectPrompt())
            }

            EmvState.CardholderConfirmation -> {
                session.start()
                session.onPrompt(cardPresented())
                session.onPrompt(confirmPrompt())
            }

            EmvState.PinEntry -> {
                session.start()
                session.onPrompt(cardPresented())
                session.onPrompt(pinPrompt())
            }

            EmvState.Online -> {
                session.start()
                session.onPrompt(cardPresented())
                session.onPrompt(onlinePrompt())
            }

            EmvState.SecondTap -> {
                session.start()
                session.onPrompt(cardPresented())
                session.onPrompt(EmvPrompt.SecondTap("retry"))
            }

            EmvState.Completed -> {
                session.start()
                session.onPrompt(cardPresented())
                session.onPrompt(finishedApproved())
            }

            EmvState.Declined -> {
                session.start()
                session.onPrompt(cardPresented())
                session.onPrompt(finishedDeclined())
            }

            EmvState.Cancelled -> {
                session.start()
                session.cancel()
            }

            EmvState.TimedOut -> {
                session.start()
                session.onTimeout()
            }

            EmvState.Failed -> {
                session.start()
                session.onCardRemoved()
            }
        }
        return session
    }

    private fun EmvTransition.awaiting(): EmvTransition.AwaitingReply {
        assertTrue(this is EmvTransition.AwaitingReply)
        return this as EmvTransition.AwaitingReply
    }

    private fun EmvTransition.resolved(): EmvTransition.Resolved {
        assertTrue(this is EmvTransition.Resolved)
        return this as EmvTransition.Resolved
    }
}
