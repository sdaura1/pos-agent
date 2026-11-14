package com.example.pos_agent.domain.card.emv

import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Serialises kernel callbacks, UI replies and cancellation into one
 * [EmvSession].
 *
 * The driver is the suspend entry point `CardAcceptor` implementations use:
 *
 * - [run] starts the session and suspends until it is terminal.
 * - [prompt] hands a kernel prompt over and returns what the adapter must send
 *   back to the vendor SDK.
 * - [cancel], [cardRemoved] and [timeout] are events, so a cancellation can
 *   preempt a suspended responder.
 * - [snapshot] lets a screen reattach while the host call is still running.
 *
 * Instances are single-use and must not be tied to an Activity lifecycle.
 */
class EmvSessionDriver(
    private val session: EmvSession = EmvSession(),
    private val timeouts: EmvTimeouts = EmvTimeouts(),
) {
    private val started = AtomicBoolean(false)
    private val events = Channel<EmvDriverEvent>(Channel.UNLIMITED)
    private val result = CompletableDeferred<AcquisitionResult>()
    private val _snapshot = MutableStateFlow(session.snapshot)
    private val _prompts = MutableSharedFlow<EmvPrompt>(extraBufferCapacity = 32)

    /** Current session view, safe to observe from the UI. */
    val snapshot: StateFlow<EmvSessionSnapshot> = _snapshot.asStateFlow()

    /** Every prompt, for logging and debugging. The snapshot remains the source of truth. */
    val prompts: SharedFlow<EmvPrompt> = _prompts.asSharedFlow()

    /**
     * Starts the session and returns its single terminal result.
     *
     * [respond] is called only for prompts that require an answer. Informational
     * prompts are accepted without calling it. If [respond] throws, the session
     * is cancelled so the kernel is not left waiting.
     */
    suspend fun run(respond: suspend (EmvPrompt) -> EmvReply): AcquisitionResult {
        check(started.compareAndSet(false, true)) {
            "EmvSessionDriver is single-use"
        }

        return coroutineScope {
            val scope = this
            var timerJob: Job? = null
            var responseJob: Job? = null
            var active: ActivePrompt? = null

            fun publish(transition: EmvTransition) {
                _snapshot.value = transition.snapshot
            }

            fun finish(resolved: EmvTransition.Resolved) {
                result.complete(resolved.result)
            }

            fun completeActive(delivery: EmvPromptDelivery) {
                active?.delivery?.complete(delivery)
                active = null
            }

            fun refreshTimer() {
                val current = session.snapshot
                val duration = when {
                    current.state == EmvState.WaitingForCard && !current.cardPresented ->
                        timeouts.cardWait

                    current.state == EmvState.SecondTap && !current.cardPresented ->
                        timeouts.secondTap

                    else -> null
                }

                if (duration == null) {
                    timerJob?.cancel()
                    timerJob = null
                } else if (timerJob?.isActive != true) {
                    timerJob = scope.launch {
                        delay(duration)
                        events.trySend(EmvDriverEvent.Timeout)
                    }
                }
            }

            fun drainQueue() {
                while (true) {
                    val queued = events.tryReceive().getOrNull() ?: break
                    if (queued is EmvDriverEvent.Prompt) {
                        queued.delivery.complete(
                            EmvPromptDelivery.Rejected(EmvRejection.SessionFinished),
                        )
                    }
                }
            }

            val start = session.start()
            try {
                check(start is EmvTransition.Accepted) {
                    "A new session must start from Idle"
                }
                publish(start)
                refreshTimer()

                while (!session.snapshot.isTerminal) {
                    when (val event = events.receive()) {
                        is EmvDriverEvent.Prompt -> {
                            val current = active
                            if (current != null) {
                                event.delivery.complete(
                                    EmvPromptDelivery.Rejected(
                                        EmvRejection.PromptAlreadyPending(
                                            current.pending,
                                            event.prompt,
                                        ),
                                    ),
                                )
                                continue
                            }

                            val transition = session.onPrompt(event.prompt)
                            publish(transition)
                            when (transition) {
                                is EmvTransition.AwaitingReply -> {
                                    _prompts.tryEmit(event.prompt)
                                    active = ActivePrompt(transition.prompt, event.delivery)
                                    val promptId = transition.prompt.id
                                    val prompt = transition.prompt.prompt
                                    responseJob = scope.launch {
                                        try {
                                            val reply = respond(prompt)
                                            events.send(
                                                EmvDriverEvent.Responded(promptId, reply),
                                            )
                                        } catch (cancellation: CancellationException) {
                                            throw cancellation
                                        } catch (error: Throwable) {
                                            events.trySend(
                                                EmvDriverEvent.RespondFailed(
                                                    promptId,
                                                    error.message,
                                                ),
                                            )
                                        }
                                    }
                                }

                                is EmvTransition.Accepted -> {
                                    _prompts.tryEmit(event.prompt)
                                    event.delivery.complete(EmvPromptDelivery.Acknowledged)
                                }

                                is EmvTransition.Resolved -> {
                                    _prompts.tryEmit(event.prompt)
                                    event.delivery.complete(EmvPromptDelivery.Acknowledged)
                                    finish(transition)
                                }

                                is EmvTransition.Rejected -> {
                                    event.delivery.complete(
                                        EmvPromptDelivery.Rejected(transition.reason),
                                    )
                                }
                            }
                        }

                        is EmvDriverEvent.Responded -> {
                            val current = active
                            if (current == null || current.pending.id != event.promptId) {
                                continue
                            }

                            val applied = session.reply(event.promptId, event.reply)
                            publish(applied)
                            when (applied) {
                                is EmvTransition.Accepted -> {
                                    current.delivery.complete(
                                        EmvPromptDelivery.Answered(event.reply),
                                    )
                                    active = null
                                }

                                is EmvTransition.Resolved -> {
                                    current.delivery.complete(
                                        EmvPromptDelivery.Answered(event.reply),
                                    )
                                    active = null
                                    finish(applied)
                                }

                                is EmvTransition.Rejected -> {
                                    current.delivery.complete(
                                        EmvPromptDelivery.Rejected(applied.reason),
                                    )
                                    active = null
                                    val cancelled = session.cancel()
                                    publish(cancelled)
                                    if (cancelled is EmvTransition.Resolved) finish(cancelled)
                                }

                                is EmvTransition.AwaitingReply -> Unit
                            }
                        }

                        is EmvDriverEvent.RespondFailed -> {
                            val current = active
                            if (current != null && current.pending.id == event.promptId) {
                                current.delivery.complete(
                                    EmvPromptDelivery.Rejected(
                                        EmvRejection.ResponderFailed(event.detail),
                                    ),
                                )
                                active = null
                            }
                            val cancelled = session.cancel()
                            publish(cancelled)
                            if (cancelled is EmvTransition.Resolved) finish(cancelled)
                        }

                        EmvDriverEvent.Cancel -> {
                            val cancelled = session.cancel()
                            publish(cancelled)
                            completeActive(EmvPromptDelivery.Answered(EmvReply.Cancel))
                            if (cancelled is EmvTransition.Resolved) finish(cancelled)
                        }

                        EmvDriverEvent.CardRemoved -> {
                            val failed = session.onCardRemoved()
                            publish(failed)
                            completeActive(EmvPromptDelivery.Answered(EmvReply.Cancel))
                            if (failed is EmvTransition.Resolved) finish(failed)
                        }

                        EmvDriverEvent.Timeout -> {
                            val timedOut = session.onTimeout()
                            publish(timedOut)
                            if (timedOut is EmvTransition.Resolved) finish(timedOut)
                        }
                    }
                    refreshTimer()
                }
            } finally {
                if (!session.snapshot.isTerminal) {
                    val cancelled = session.cancel()
                    publish(cancelled)
                    if (cancelled is EmvTransition.Resolved) {
                        completeActive(EmvPromptDelivery.Answered(EmvReply.Cancel))
                        result.complete(cancelled.result)
                    }
                }
                events.close()
                timerJob?.cancel()
                responseJob?.cancel()
                completeActive(EmvPromptDelivery.Rejected(EmvRejection.SessionFinished))
                drainQueue()
            }

            result.await()
        }
    }

    /**
     * Hands a kernel prompt to the session.
     *
     * The call suspends until the app has answered. The result tells the
     * adapter whether to call the vendor `set*Response`, do nothing, or stop the
     * kernel.
     */
    suspend fun prompt(prompt: EmvPrompt): EmvPromptDelivery {
        if (!started.get()) {
            return EmvPromptDelivery.Rejected(EmvRejection.SessionNotStarted)
        }
        if (_snapshot.value.isTerminal) {
            return EmvPromptDelivery.Rejected(EmvRejection.SessionFinished)
        }

        val delivery = CompletableDeferred<EmvPromptDelivery>()
        val sent = events.trySend(EmvDriverEvent.Prompt(prompt, delivery))
        if (sent.isFailure) {
            return EmvPromptDelivery.Rejected(EmvRejection.SessionFinished)
        }
        return delivery.await()
    }

    /**
     * Cancels the session and waits for its terminal result.
     *
     * The pending prompt, if any, is answered with [EmvReply.Cancel] and that
     * reply is returned from the suspended [prompt] call.
     */
    suspend fun cancel(): AcquisitionResult {
        if (!started.get()) return AcquisitionResult.Cancelled
        events.trySend(EmvDriverEvent.Cancel)
        return result.await()
    }

    /** Fails the session because the card left the reader. */
    fun cardRemoved() {
        if (started.get()) events.trySend(EmvDriverEvent.CardRemoved)
    }

    /** Reports an expired card wait or second-tap deadline. */
    fun timeout() {
        if (started.get()) events.trySend(EmvDriverEvent.Timeout)
    }

    private data class ActivePrompt(
        val pending: PendingPrompt,
        val delivery: CompletableDeferred<EmvPromptDelivery>,
    )

    private sealed interface EmvDriverEvent {
        data class Prompt(
            val prompt: EmvPrompt,
            val delivery: CompletableDeferred<EmvPromptDelivery>,
        ) : EmvDriverEvent

        data class Responded(
            val promptId: Long,
            val reply: EmvReply,
        ) : EmvDriverEvent

        data class RespondFailed(
            val promptId: Long,
            val detail: String?,
        ) : EmvDriverEvent

        data object Cancel : EmvDriverEvent

        data object CardRemoved : EmvDriverEvent

        data object Timeout : EmvDriverEvent
    }
}
