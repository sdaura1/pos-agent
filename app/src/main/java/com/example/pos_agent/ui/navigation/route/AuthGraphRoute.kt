package com.example.pos_agent.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface AuthGraphRoute {
    @Serializable data object Login : AuthGraphRoute
    @Serializable data object ResetPin : AuthGraphRoute
    @Serializable data class SetPin(val username: String) : AuthGraphRoute
}
