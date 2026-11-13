package com.example.pos_agent.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    @Serializable
    data object Splash : AppRoute

    @Serializable
    data object AuthGraph : AppRoute

    @Serializable
    data object MainGraph : AppRoute
}
