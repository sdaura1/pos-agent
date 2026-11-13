package com.example.pos_agent.ui.navigation.route

import kotlinx.serialization.Serializable

sealed interface MainGraphRoute {
    @Serializable data object Dashboard : MainGraphRoute
    @Serializable data object TransactionHistory : MainGraphRoute
    @Serializable data object Amount : MainGraphRoute
    @Serializable data object Profile : MainGraphRoute
}
