package com.example.pos_agent.ui.navigation.navGraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.ui.navigation.route.MainGraphRoute
import com.example.pos_agent.ui.navigation.route.ProfileGraph
import com.example.pos_agent.ui.profile.ProfileScreen

fun NavGraphBuilder.profileGraph(
    session: AuthSession,
    onLogout: () -> Unit,
) {
    navigation<ProfileGraph>(
        startDestination = MainGraphRoute.Profile,
    ) {
        composable<MainGraphRoute.Profile> {
            ProfileScreen(
                user = session.user,
                onLogout = onLogout,
            )
        }
    }
}
