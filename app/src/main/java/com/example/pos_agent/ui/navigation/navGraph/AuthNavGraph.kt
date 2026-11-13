package com.example.pos_agent.ui.navigation.navGraph

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.ui.auth.LoginScreen
import com.example.pos_agent.ui.auth.LoginViewModel
import com.example.pos_agent.ui.auth.ResetPinScreen
import com.example.pos_agent.ui.auth.SetPinScreen
import com.example.pos_agent.ui.navigation.route.AppRoute
import com.example.pos_agent.ui.navigation.route.AuthGraphRoute

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onAuthenticated: (AuthSession) -> Unit,
) {
    navigation<AppRoute.AuthGraph>(
        startDestination = AuthGraphRoute.Login,
    ) {
        composable<AuthGraphRoute.Login> {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onAuthenticated = onAuthenticated,
                onResetPinClick = {
                    navController.navigate(AuthGraphRoute.ResetPin)
                },
            )
        }

        composable<AuthGraphRoute.ResetPin> {
            ResetPinScreen(
                onContinue = { username ->
                    navController.navigate(AuthGraphRoute.SetPin(username))
                },
            )
        }

        composable<AuthGraphRoute.SetPin> { entry ->
            val route = entry.toRoute<AuthGraphRoute.SetPin>()
            SetPinScreen(
                username = route.username,
                onPinSet = { _ ->
                    // TODO: Complete the authenticated session after PIN setup once the PIN API is wired.
                    navController.navigate(AuthGraphRoute.Login) {
                        popUpTo(AuthGraphRoute.Login) { inclusive = true }
                    }
                },
            )
        }
    }
}
