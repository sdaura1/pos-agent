package com.example.pos_agent.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.ui.navigation.animations.EnterExitTransitions
import com.example.pos_agent.ui.navigation.navGraph.amountGraph
import com.example.pos_agent.ui.navigation.navGraph.dashboardDestination
import com.example.pos_agent.ui.navigation.navGraph.profileGraph
import com.example.pos_agent.ui.navigation.navGraph.transactionGraph
import com.example.pos_agent.ui.navigation.route.MainGraphRoute

@Composable
fun MainNavHost(
    navController: NavHostController,
    session: AuthSession,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MainGraphRoute.Dashboard,
        modifier = modifier,
        enterTransition = { EnterExitTransitions.SlideInFromRight },
        exitTransition = { EnterExitTransitions.SlideOutToLeft },
        popEnterTransition = { EnterExitTransitions.SlideInFromLeft },
        popExitTransition = { EnterExitTransitions.SlideOutToRight },
    ) {
        dashboardDestination(navController = navController, session = session)
        transactionGraph()
        amountGraph()
        profileGraph(session = session, onLogout = onLogout)
    }
}
