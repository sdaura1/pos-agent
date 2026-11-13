package com.example.pos_agent.domain.auth.model

/**
 * Opaque access or refresh token returned by the auth service.
 *
 * [toString] is masked so a token cannot leak into logs or crash reports.
 */
@JvmInline
value class AuthToken(private val value: String) {
    init {
        require(value.isNotBlank()) { "Auth token must not be blank" }
    }

    val raw: String get() = value

    override fun toString(): String = "AuthToken(***)"
}
