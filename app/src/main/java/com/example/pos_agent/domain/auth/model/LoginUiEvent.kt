package com.example.pos_agent.domain.auth.model

sealed interface LoginUiEvent {
    data class Success(val session: AuthSession) : LoginUiEvent
    data class LoginError(val messageResId: Int) : LoginUiEvent
}
