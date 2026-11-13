package com.example.pos_agent.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pos_agent.core.result.Response
import com.example.pos_agent.domain.auth.model.LoginUiEvent
import com.example.pos_agent.domain.auth.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Owns the login form and emits a one-shot event when sign-in succeeds. */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<LoginUiEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<LoginUiEvent> = _events.asSharedFlow()

    fun onUsernameChange(value: String) {
        _state.update { it.copy(username = value, usernameError = null, error = null) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, passwordError = null, error = null) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun submit() {
        val current = _state.value
        if (current.isSubmitting) return

        val username = current.username.trim()
        val usernameError = if (username.isEmpty()) LoginFieldError.USERNAME_REQUIRED else null
        val passwordError = if (current.password.isEmpty()) LoginFieldError.PASSWORD_REQUIRED else null
        if (usernameError != null || passwordError != null) {
            _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true, error = null) }
            when (val result = loginUseCase(username, current.password)) {
                is Response.Success -> {
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            username = "",
                            password = "",
                            isPasswordVisible = false,
                        )
                    }
                    _events.emit(LoginUiEvent.Success(result.value))
                }

                is Response.Failure -> _state.update {
                    it.copy(isSubmitting = false, error = result.error)
                }
            }
        }
    }
}
