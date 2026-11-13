package com.example.pos_agent.ui.navigation.navGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pos_agent.ui.amount.AmountScreen
import com.example.pos_agent.ui.navigation.route.AmountGraph
import com.example.pos_agent.ui.navigation.route.MainGraphRoute

fun NavGraphBuilder.amountGraph() {
    navigation<AmountGraph>(
        startDestination = MainGraphRoute.Amount,
    ) {
        composable<MainGraphRoute.Amount> {
            AmountScreen()
        }
    }
}
