package com.example.pos_agent.domain.auth.model

/** A signed-in session; expiry is checked locally before restoring it. */
data class AuthSession(
    val user: User,
    val accessToken: AuthToken,
    val refreshToken: AuthToken? = null,
    val expiresAtEpochMillis: Long,
) {
    fun isExpired(nowEpochMillis: Long): Boolean = nowEpochMillis >= expiresAtEpochMillis
}
