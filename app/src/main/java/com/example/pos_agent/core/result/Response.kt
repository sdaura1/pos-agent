package com.example.pos_agent.core.result

import com.example.pos_agent.core.error.DomainError

/**
 * The single success/failure envelope used at every boundary in the app.
 *
 * NIBSS (REST or ISO 8583), local database, and socket adapters all translate
 * their native results and exceptions into [Success] or [Failure] before the
 * value crosses into the domain. Domain code therefore never depends on
 * transport-specific types.
 *
 * Named `Response` rather than `Result` to avoid shadowing [kotlin.Result].
 */
sealed interface Response<out T> {
    data class Success<out T>(val value: T) : Response<T>
    data class Failure(val error: DomainError) : Response<Nothing>

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }
}

inline fun <T, R> Response<T>.map(transform: (T) -> R): Response<R> = when (this) {
    is Response.Success -> Response.Success(transform(value))
    is Response.Failure -> this
}

inline fun <T> Response<T>.onSuccess(action: (T) -> Unit): Response<T> {
    if (this is Response.Success) action(value)
    return this
}

inline fun <T> Response<T>.onFailure(action: (DomainError) -> Unit): Response<T> {
    if (this is Response.Failure) action(error)
    return this
}
