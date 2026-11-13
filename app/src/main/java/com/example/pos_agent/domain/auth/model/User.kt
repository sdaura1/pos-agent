package com.example.pos_agent.domain.auth.model

/** The operator signed in to this terminal. */
data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val role: String,
)
