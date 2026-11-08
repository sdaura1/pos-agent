package com.example.pos_agent.core.result

import com.example.pos_agent.core.error.DomainError

/**
 * Explicit success/failure type used across layers.
 *
 * Named `Outcome` rather than `Result` to avoid shadowing [kotlin.Result].
 */
sealed interface Outcome<out T> {
    data class Success<out T>(val value: T) : Outcome<T>
    data class Failure(val error: DomainError) : Outcome<Nothing>

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }
}

inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> = when (this) {
    is Outcome.Success -> Outcome.Success(transform(value))
    is Outcome.Failure -> this
}

inline fun <T> Outcome<T>.onSuccess(action: (T) -> Unit): Outcome<T> {
    if (this is Outcome.Success) action(value)
    return this
}

inline fun <T> Outcome<T>.onFailure(action: (DomainError) -> Unit): Outcome<T> {
    if (this is Outcome.Failure) action(error)
    return this
}
