package com.example.pos_agent.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    @Serializable
    data object AuthGraph: AppRoute

    @Serializable
    data class MainGraph(
        val username: String,
        val displayName: String,
        val userId: String,
        val role: String,
        ): AppRoute
}
