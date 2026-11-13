package com.example.pos_agent.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.pos_agent.ui.main.MainViewModel
import com.example.pos_agent.ui.navigation.navGraph.authNavGraph
import com.example.pos_agent.ui.navigation.route.AppRoute

@Composable
fun AuthNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.AuthGraph,
        modifier = modifier,
    ) {
        authNavGraph(
            navController = navController,
            onAuthenticated = mainViewModel::onAuthenticated,
        )
    }
}
