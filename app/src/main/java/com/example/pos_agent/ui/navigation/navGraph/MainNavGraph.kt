package com.example.pos_agent.ui.navigation.navGraph

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pos_agent.ui.amount.AmountScreen
import com.example.pos_agent.ui.home.HomeScreen
import com.example.pos_agent.ui.main.MainViewModel
import com.example.pos_agent.ui.navigation.LoadingScreen
import com.example.pos_agent.ui.navigation.animations.EnterExitTransitions
import com.example.pos_agent.ui.navigation.extensions.fadeComposable
import com.example.pos_agent.ui.navigation.route.AppRoute
import com.example.pos_agent.ui.navigation.route.MainGraphRoute
import com.example.pos_agent.ui.profile.ProfileScreen
import com.example.pos_agent.ui.transaction.TransactionHistoryScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    mainViewModel: MainViewModel,
    onLogout: () -> Unit,
) {
    navigation<AppRoute.MainGraph>(
        startDestination = MainGraphRoute.Dashboard,
        enterTransition = { EnterExitTransitions.SlideInFromRight },
        exitTransition = { EnterExitTransitions.SlideOutToLeft },
        popEnterTransition = { EnterExitTransitions.SlideInFromLeft },
        popExitTransition = { EnterExitTransitions.SlideOutToRight },
    ) {
        fadeComposable<MainGraphRoute.Dashboard> {
            val session by mainViewModel.session.collectAsStateWithLifecycle()
            val currentSession = session
            if (currentSession == null) {
                LoadingScreen()
            } else {
                HomeScreen(
                    user = currentSession.user,
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

        composable<MainGraphRoute.TransactionHistory> {
            TransactionHistoryScreen()
        }

        composable<MainGraphRoute.Amount> {
            AmountScreen()
        }

        composable<MainGraphRoute.Profile> {
            val session by mainViewModel.session.collectAsStateWithLifecycle()
            val currentSession = session
            if (currentSession == null) {
                LoadingScreen()
            } else {
                ProfileScreen(
                    user = currentSession.user,
                    onLogout = onLogout,
                )
            }
        }
    }
}
