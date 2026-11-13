package com.example.pos_agent.ui.navigation.navGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pos_agent.ui.navigation.route.MainGraphRoute
import com.example.pos_agent.ui.navigation.route.TransactionGraph
import com.example.pos_agent.ui.transaction.TransactionHistoryScreen

fun NavGraphBuilder.transactionGraph() {
    navigation<TransactionGraph>(
        startDestination = MainGraphRoute.TransactionHistory,
    ) {
        composable<MainGraphRoute.TransactionHistory> {
            TransactionHistoryScreen()
        }
    }
}
