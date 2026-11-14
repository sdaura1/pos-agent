package com.example.pos_agent.domain.card.emv

import com.example.pos_agent.domain.card.model.CardCapture
import com.example.pos_agent.domain.card.model.CardEntryMode
import com.example.pos_agent.domain.card.model.MaskedPan
import com.example.pos_agent.domain.shared.Money

/**
 * One step in the EMV dialogue, emitted by the adapter towards the app.
 *
 * [CardPresented], [SecondTap], [KernelSelected] and [Finished] are
 * informational: the app observes them but must not reply. The other prompts
 * are answerable and remain pending until the app replies exactly once.
 */
sealed interface EmvPrompt {
    /** The terminal read a card. Informational. */
    data class CardPresented(
        val entryMode: CardEntryMode,
        val cardSerial: String? = null,
    ) : EmvPrompt

    /** Several applications matched and the cardholder must choose one. */
    data class SelectApplication(
        val applications: List<String>,
        val firstSelect: Boolean,
    ) : EmvPrompt {
        init {
            require(applications.isNotEmpty()) {
                "At least one application label is required"
            }
            require(applications.none { it.isBlank() }) {
                "Application labels cannot be blank"
            }
        }
    }

    /** The kernel needs amount, CVM or issuer confirmation. */
    data class ConfirmCardInfo(
        val reason: Reason,
        val maskedPan: MaskedPan,
        val applicationLabel: String? = null,
        val amount: Money? = null,
    ) : EmvPrompt {
        enum class Reason {
            AMOUNT,
            CVM,
            ISSUER_REFERRAL,
            GPO_FILTER,
        }
    }

    /** The kernel needs a PIN. */
    data class RequestPin(
        val mode: Mode,
        val amount: Money,
        val attemptsRemaining: Int? = null,
    ) : EmvPrompt {
        init {
            require(attemptsRemaining == null || attemptsRemaining > 0) {
                "Attempts remaining must be positive when known"
            }
        }

        enum class Mode {
            ONLINE,
            OFFLINE,
        }
    }

    /**
     * The kernel produced everything the host needs.
     *
     * This is the only prompt that causes a network call, and the draft
     * guarantees a masked PAN and EMV TLV are present.
     */
    data class RequestOnline(val draft: CardCaptureDraft) : EmvPrompt

    /** The contactless card must be presented again. Informational. */
    data class SecondTap(val reason: String? = null) : EmvPrompt

    /** The kernel selected a scheme. Informational. */
    data class KernelSelected(val scheme: String) : EmvPrompt {
        init {
            require(scheme.isNotBlank()) { "Kernel scheme cannot be blank" }
        }
    }

    /** The kernel finished and produced a capture. Informational and terminal. */
    data class Finished(
        val decision: EmvDecision,
        val capture: CardCapture,
    ) : EmvPrompt

    /** The kernel or adapter failed. Informational and terminal. */
    data class Failure(
        val code: String,
        val detail: String? = null,
    ) : EmvPrompt {
        init {
            require(code.isNotBlank()) { "Failure code cannot be blank" }
        }
    }
}
