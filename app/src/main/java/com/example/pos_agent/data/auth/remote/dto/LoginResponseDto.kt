package com.example.pos_agent.data.auth.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresInSeconds: Long = 3600,
    val user: UserDto,
)
