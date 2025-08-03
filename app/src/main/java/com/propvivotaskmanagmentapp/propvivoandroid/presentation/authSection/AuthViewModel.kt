package com.propvivotaskmanagmentapp.propvivoandroid.presentation.authSection

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.PreferenceDataStoreHelper
import com.propvivotaskmanagmentapp.propvivoandroid.data.local.datastore.dsConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.PreferenceDataStoreInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.RegisterUseCase
import com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val registerUseCase: RegisterUseCase,
    private val dataStoreHelper: PreferenceDataStoreInterface
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
            is AuthEvent.NameChanged -> state = state.copy(name = event.name)
        }
    }

    private fun submit() = viewModelScope.launch {
        state = state.copy(isLoading = true, errorMessage = null)

        try {
            val resultFlow = if (state.isSignIn) {
                signInUseCase.invoke(state.email, state.password)
            } else {
                registerUseCase.invoke(state.email, state.name, state.password, state.role)
            }

            resultFlow.collectLatest { result ->
                result
                    .onSuccess {
                        dataStoreHelper.putPreference(dsConstants.USER_ID, it.uid)
                        dataStoreHelper.putPreference(dsConstants.USER_ROLE, it.role)
                        dataStoreHelper.putPreference(dsConstants.USER_NAME, it.name)
                        dataStoreHelper.putPreference(dsConstants.USER_EMAIL, it.email)
                        // TODO: Navigate to Home screen
                        Log.e("AuthViewModel", "User logged in successfully")
                    }
                    .onFailure {
                        state = state.copy(errorMessage = it.message)
                    }
            }
        } catch (e: Exception) {
            state = state.copy(errorMessage = e.message ?: "Unknown error")
        } finally {
            state = state.copy(isLoading = false)
        }
    }

}
