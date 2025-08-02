package com.propvivotaskmanagmentapp.propvivoandroid.presentation.authSection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.RegisterUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> state = state.copy(email = event.email)
            is AuthEvent.PasswordChanged -> state = state.copy(password = event.password)
            is AuthEvent.RoleChanged -> state = state.copy(role = event.role)
            is AuthEvent.ToggleAuthMode -> state = state.copy(isSignIn = event.isSignIn)
            is AuthEvent.Submit -> submit()
        }
    }

    private fun submit() = viewModelScope.launch {
        state = state.copy(isLoading = true, errorMessage = null)

        val resultFlow = if (state.isSignIn) {
            signInUseCase.invoke(state.email, state.password)
        } else {
            registerUseCase.invoke(state.email, state.password, state.role)
        }

        resultFlow.collect { result ->
            result.onSuccess {
                state = state.copy(isLoading = false)
                // TODO: Navigate to Home screen
            }.onFailure {
                state = state.copy(isLoading = false, errorMessage = it.message)
            }
        }
    }
}
