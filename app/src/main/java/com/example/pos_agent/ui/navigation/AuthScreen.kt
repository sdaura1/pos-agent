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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pos_agent.R
import com.example.pos_agent.ui.main.MainViewModel
import com.example.pos_agent.ui.navigation.route.AuthGraphRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val titleRes = when {
        currentDestination?.hasRoute(AuthGraphRoute.ResetPin::class) == true ->
            R.string.reset_pin_title

        currentDestination?.hasRoute(AuthGraphRoute.SetPin::class) == true ->
            R.string.set_pin_title

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
        AuthNavHost(
            navController = navController,
            mainViewModel = mainViewModel,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
