package com.example.pos_agent.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pos_agent.R
import com.example.pos_agent.ui.main.AppAuthState
import com.example.pos_agent.ui.main.MainViewModel
import com.example.pos_agent.ui.navigation.navGraph.authNavGraph
import com.example.pos_agent.ui.navigation.navGraph.mainNavGraph
import com.example.pos_agent.ui.navigation.route.AppRoute
import com.example.pos_agent.ui.navigation.route.AuthGraphRoute
import com.example.pos_agent.ui.navigation.route.MainGraphRoute
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val uiState by mainViewModel.loadState.collectAsStateWithLifecycle()
    val session by mainViewModel.session.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.authState, session) {
        val authState = uiState.authState ?: return@LaunchedEffect
        when {
            authState == AppAuthState.AUTHENTICATED && session != null -> {
                if (!navController.isInGraph(AppRoute.MainGraph::class)) {
                    navController.navigate(AppRoute.MainGraph) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            authState == AppAuthState.UNAUTHENTICATED && session == null -> {
                if (!navController.isInGraph(AppRoute.AuthGraph::class)) {
                    navController.navigate(AppRoute.AuthGraph) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val titleRes = when {
        currentDestination?.hasRoute(AuthGraphRoute.ResetPin::class) == true ->
            R.string.reset_pin_title

        currentDestination?.hasRoute(AuthGraphRoute.SetPin::class) == true ->
            R.string.set_pin_title

        currentDestination?.hasRoute(MainGraphRoute.TransactionHistory::class) == true ->
            R.string.transaction_history_title

        currentDestination?.hasRoute(MainGraphRoute.Amount::class) == true ->
            R.string.amount_title

        currentDestination?.hasRoute(MainGraphRoute.Profile::class) == true ->
            R.string.profile_title

        else -> null
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (titleRes != null) {
                TopAppBar(
                    title = { Text(stringResource(titleRes)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.nav_back),
                            )
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Splash,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            composable<AppRoute.Splash> {
                LoadingScreen()
            }

            authNavGraph(
                navController = navController,
                onAuthenticated = mainViewModel::onAuthenticated,
            )

            mainNavGraph(
                navController = navController,
                mainViewModel = mainViewModel,
                onLogout = mainViewModel::logout,
            )
        }
    }
}

private fun NavHostController.isInGraph(graphRoute: KClass<*>): Boolean =
    currentBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(graphRoute) } == true
