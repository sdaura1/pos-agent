package com.example.pos_agent.ui.navigation.navGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.ui.home.HomeScreen
import com.example.pos_agent.ui.navigation.extensions.fadeComposable
import com.example.pos_agent.ui.navigation.route.MainGraphRoute

fun NavGraphBuilder.dashboardDestination(
    navController: NavController,
    session: AuthSession,
) {
    fadeComposable<MainGraphRoute.Dashboard> {
        HomeScreen(
            user = session.user,
            onTransactionHistoryClick = {
                navController.navigate(MainGraphRoute.TransactionHistory)
            },
            onAmountClick = {
                navController.navigate(MainGraphRoute.Amount)
            },
            onProfileClick = {
                navController.navigate(MainGraphRoute.Profile)
            },
        )
    }
}
