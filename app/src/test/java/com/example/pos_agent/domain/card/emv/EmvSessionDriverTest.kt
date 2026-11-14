package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardCaptureErrorCode
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EmvSessionDriverTest {

    @Test
    fun `run should approve when the kernel finishes after online approval`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { prompt ->
                if (prompt is EmvPrompt.RequestOnline) {
                    EmvReply.HostApproved()
                } else {
                    EmvReply.Cancel
                }
            }
        }
        runCurrent()

        // Act
        driver.prompt(cardPresented())
        driver.prompt(onlinePrompt())
        driver.prompt(finishedApproved())

        // Assert
        assertEquals(AcquisitionResult.Approved(capture()), result.await())
    }

    @Test
    fun `run should decline when the kernel declines after the host`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { prompt ->
                if (prompt is EmvPrompt.RequestOnline) {
                    EmvReply.HostDeclined()
                } else {
                    EmvReply.Cancel
                }
            }
        }
        runCurrent()

        // Act
        driver.prompt(cardPresented())
        driver.prompt(onlinePrompt())
        driver.prompt(finishedDeclined())

        // Assert
        assertEquals(AcquisitionResult.Declined(capture()), result.await())
    }

    @Test
    fun `cancel should answer the pending prompt when pin entry is open`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { awaitCancellation() }
        }
        runCurrent()
        driver.prompt(cardPresented())
        val pinDelivery = async { driver.prompt(pinPrompt()) }
        runCurrent()

        // Act
        val cancelResult = driver.cancel()
        val delivered = pinDelivery.await()

        // Assert
        assertEquals(EmvPromptDelivery.Answered(EmvReply.Cancel), delivered)
        assertEquals(AcquisitionResult.Cancelled, cancelResult)
        assertEquals(AcquisitionResult.Cancelled, result.await())
    }

    @Test
    fun `run should cancel the session when the caller cancels the driver coroutine`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val run = async {
            driver.run { awaitCancellation() }
        }
        runCurrent()
        driver.prompt(cardPresented())
        val pinDelivery = async { driver.prompt(pinPrompt()) }
        runCurrent()

        // Act
        run.cancel()
        run.join()

        // Assert
        assertEquals(EmvPromptDelivery.Answered(EmvReply.Cancel), pinDelivery.await())
        assertEquals(AcquisitionResult.Cancelled, driver.snapshot.value.result)
    }

    @Test
    fun `run should time out when no card is presented before the deadline`() = runTest {
        // Arrange
        val driver = EmvSessionDriver(
            timeouts = EmvTimeouts(cardWait = 5.seconds, secondTap = 5.seconds),
        )
        val result = async {
            driver.run { EmvReply.Cancel }
        }
        runCurrent()

        // Act
        advanceTimeBy(5.seconds + 1.milliseconds)
        runCurrent()

        // Assert
        assertEquals(
            AcquisitionResult.TimedOut(AcquisitionTimeout.CARD_WAIT),
            result.await(),
        )
    }

    @Test
    fun `cardRemoved should fail the session and answer the pending prompt`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { awaitCancellation() }
        }
        runCurrent()
        driver.prompt(cardPresented())
        val pinDelivery = async { driver.prompt(pinPrompt()) }
        runCurrent()

        // Act
        driver.cardRemoved()
        val delivered = pinDelivery.await()

        // Assert
        assertEquals(EmvPromptDelivery.Answered(EmvReply.Cancel), delivered)
        assertEquals(
            AcquisitionResult.Failed(CardCaptureErrorCode.CARD_REMOVED),
            result.await(),
        )
    }

    @Test
    fun `prompt should be rejected when the session has finished`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { EmvReply.Cancel }
        }
        runCurrent()
        driver.prompt(cardPresented())
        driver.prompt(finishedApproved())
        result.await()

        // Act
        val delivery = driver.prompt(cardPresented())

        // Assert
        assertTrue(delivery is EmvPromptDelivery.Rejected)
        assertEquals(
            EmvRejection.SessionFinished,
            (delivery as EmvPromptDelivery.Rejected).reason,
        )
    }

    @Test
    fun `prompt should be rejected when the driver has not started`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()

        // Act
        val delivery = driver.prompt(cardPresented())

        // Assert
        assertTrue(delivery is EmvPromptDelivery.Rejected)
        assertEquals(
            EmvRejection.SessionNotStarted,
            (delivery as EmvPromptDelivery.Rejected).reason,
        )
    }

    @Test
    fun `run should stay online when the host response is delayed`() = runTest {
        // Arrange
        val host = CompletableDeferred<EmvReply>()
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { prompt ->
                if (prompt is EmvPrompt.RequestOnline) {
                    host.await()
                } else {
                    EmvReply.Cancel
                }
            }
        }
        runCurrent()
        driver.prompt(cardPresented())
        val onlineDelivery = async { driver.prompt(onlinePrompt()) }
        runCurrent()

        // Act
        val state = driver.snapshot.value

        // Assert
        assertEquals(EmvState.Online, state.state)
        assertNotNull(state.pendingPrompt)

        host.complete(EmvReply.HostApproved())
        assertEquals(
            EmvPromptDelivery.Answered(EmvReply.HostApproved()),
            onlineDelivery.await(),
        )
        driver.prompt(finishedApproved())
        assertEquals(AcquisitionResult.Approved(capture()), result.await())
    }

    @Test
    fun `run should cancel when the responder sends a disallowed reply`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { EmvReply.Select(0) }
        }
        runCurrent()
        driver.prompt(cardPresented())

        // Act
        val delivery = driver.prompt(pinPrompt())

        // Assert
        assertTrue(delivery is EmvPromptDelivery.Rejected)
        assertTrue(
            (delivery as EmvPromptDelivery.Rejected).reason
                is EmvRejection.DisallowedReply,
        )
        assertEquals(AcquisitionResult.Cancelled, result.await())
    }

    @Test
    fun `prompt should not call the responder when the prompt is informational`() = runTest {
        // Arrange
        val driver = EmvSessionDriver()
        val result = async {
            driver.run { error("the responder must not be called") }
        }
        runCurrent()

        // Act
        val delivery = driver.prompt(cardPresented())

        // Assert
        assertEquals(EmvPromptDelivery.Acknowledged, delivery)
        assertEquals(AcquisitionResult.Cancelled, driver.cancel())
        assertEquals(AcquisitionResult.Cancelled, result.await())
    }
}
