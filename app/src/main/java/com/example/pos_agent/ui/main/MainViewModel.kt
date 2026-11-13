package com.example.pos_agent.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.AuthSession
import com.example.pos_agent.domain.auth.usecase.LogoutUseCase
import com.example.pos_agent.domain.auth.usecase.RestoreSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Activity-scoped owner of app-wide load and auth state. */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val restoreSessionUseCase: RestoreSessionUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _loadState = MutableStateFlow(MainUiState())
    val loadState: StateFlow<MainUiState> = _loadState.asStateFlow()

    private val _session = MutableStateFlow<AuthSession?>(null)
    val session: StateFlow<AuthSession?> = _session.asStateFlow()

    init {
        viewModelScope.launch {
            val restored = when (val result = restoreSessionUseCase()) {
                is Response.Success -> result.value
                is Response.Failure -> null
            }
            _session.value = restored
            _loadState.value = MainUiState(
                loadState = MainLoadState.Loaded(
                    appAuthState = if (restored != null) {
                        AppAuthState.AUTHENTICATED
                    } else {
                        AppAuthState.UNAUTHENTICATED
                    },
                ),
            )
        }
    }

    fun onAuthenticated(session: AuthSession) {
        _session.value = session
        _loadState.value = MainUiState(
            loadState = MainLoadState.Loaded(AppAuthState.AUTHENTICATED)
        )
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _session.value = null
            _loadState.value = MainUiState(
                loadState = MainLoadState.Loaded(AppAuthState.UNAUTHENTICATED)
            )
        }
    }
}
