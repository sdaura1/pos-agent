package com.example.pos_agent.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.model.User
import com.example.pos_agent.ui.amount.AmountScreen
import com.example.pos_agent.ui.home.HomeScreen
import com.example.pos_agent.ui.main.AppAuthState
import com.example.pos_agent.ui.main.MainViewModel
import com.example.pos_agent.ui.navigation.navGraph.authNavGraph
import com.example.pos_agent.ui.navigation.route.AppRoute
import com.example.pos_agent.ui.navigation.route.MainGraphRoute
import com.example.pos_agent.ui.profile.ProfileScreen
import com.example.pos_agent.ui.transaction.TransactionHistoryScreen

@Composable
fun AppRoot(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val uiState by mainViewModel.loadState.collectAsStateWithLifecycle()
    val session by mainViewModel.session.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingScreen(modifier = modifier)
        return
    }

    val startDestination: Any = if (uiState.authState == AppAuthState.AUTHENTICATED) {
        val currentSession = session
        if (currentSession == null) {
            LoadingScreen(modifier = modifier)
            return
        }
        currentSession.toMainGraphRoute()
    } else {
        AppRoute.AuthGraph
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize(),
    ) {
        authNavGraph(
            navController = navController,
            onAuthenticated = { authSession ->
                mainViewModel.onAuthenticated(authSession)
                navController.navigate(authSession.toMainGraphRoute()) {
                    popUpTo(AppRoute.AuthGraph) { inclusive = true }
                }
            },
        )

        composable<AppRoute.MainGraph> { entry ->
            val route = entry.toRoute<AppRoute.MainGraph>()
            HomeScreen(
                user = route.toUser(),
                onAmountClick = {
                    navController.navigate(MainGraphRoute.Amount)
                },
                onProfileClick = {
                    navController.navigate(MainGraphRoute.Profile)
                },
                onTransactionHistoryClick = {
                    navController.navigate(MainGraphRoute.TransactionHistory)
                },
            )
        }

        composable<MainGraphRoute.TransactionHistory> {
            TransactionHistoryScreen()
        }

        composable<MainGraphRoute.Amount> {
            AmountScreen()
        }

        composable<MainGraphRoute.Profile> {
            val currentSession = session
            if (currentSession != null) {
                ProfileScreen(
                    user = currentSession.user,
                    onLogout = {
                        mainViewModel.logout()
                        navController.navigate(AppRoute.AuthGraph) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}

private fun AuthSession.toMainGraphRoute() = AppRoute.MainGraph(
    username = user.username,
    displayName = user.displayName,
    userId = user.id,
    role = user.role,
)

private fun AppRoute.MainGraph.toUser() = User(
    id = userId,
    username = username,
    displayName = displayName,
    role = role,
)

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
