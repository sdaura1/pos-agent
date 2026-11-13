package com.example.pos_agent.ui.main

import androidx.compose.runtime.Stable

sealed interface MainLoadState {
    data object Loading : MainLoadState

    data class Loaded(
        val appAuthState: AppAuthState,
    ) : MainLoadState
}


@Stable
data class MainUiState(
    val loadState: MainLoadState = MainLoadState.Loading,
) {
    val authState: AppAuthState?
        get() = (loadState as? MainLoadState.Loaded)?.appAuthState

    val isLoading: Boolean
        get() = loadState is MainLoadState.Loading
}