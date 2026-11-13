package com.example.pos_agent.data.auth.remote.mapper

import com.example.pos_agent.data.auth.remote.dto.LoginResponseDto
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.model.AuthToken
import com.example.pos_agent.domain.auth.model.User

object AuthMapper {
    fun toSession(dto: LoginResponseDto, nowEpochMillis: Long): AuthSession {
        require(dto.accessToken.isNotBlank()) { "accessToken is required" }
        require(dto.user.id.isNotBlank()) { "user.id is required" }
        require(dto.user.username.isNotBlank()) { "user.username is required" }

        return AuthSession(
            user = User(
                id = dto.user.id,
                username = dto.user.username,
                displayName = dto.user.displayName.ifBlank { dto.user.username },
                role = dto.user.role.ifBlank { DEFAULT_ROLE },
            ),
            accessToken = AuthToken(dto.accessToken),
            refreshToken = dto.refreshToken
                ?.takeIf(String::isNotBlank)
                ?.let { AuthToken(it) },
            expiresAtEpochMillis = nowEpochMillis + dto.expiresInSeconds.coerceAtLeast(0L) * 1000L,
        )
    }

    private const val DEFAULT_ROLE = "OPERATOR"
}
