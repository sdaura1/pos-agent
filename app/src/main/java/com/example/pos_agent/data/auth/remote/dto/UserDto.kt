package com.example.pos_agent.data.auth.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val displayName: String = "",
    val role: String = "OPERATOR",
)
