package com.example.pos_agent.core.error

/**
 * Errors the domain understands. Data-layer exceptions (HTTP, SQLite, IO)
 * must be translated into these before crossing the boundary.
 */
sealed interface DomainError {
    val message: String

    /** Stable identifier the UI can map to a localized message. */
    val code: String? get() = null

    data class Validation(
        override val message: String,
        override val code: String? = null,
    ) : DomainError

    data class NotFound(
        override val message: String,
        override val code: String? = null,
    ) : DomainError

    data class Conflict(
        override val message: String,
        override val code: String? = null,
    ) : DomainError

    data class Network(
        override val message: String,
        val cause: Throwable? = null,
        override val code: String? = null,
    ) : DomainError

    data class Unknown(
        override val message: String,
        val cause: Throwable? = null,
        override val code: String? = null,
    ) : DomainError
}
