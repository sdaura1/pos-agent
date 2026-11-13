package com.example.pos_agent.domain.auth.model

/**
 * Credentials crossing into the domain for authentication.
 *
 * [toString] never prints the password so accidental logging is harmless.
 */
data class LoginCredentials(
    val username: String,
    val password: String,
) {
    override fun toString(): String = "LoginCredentials(username=$username, password=***)"
}
