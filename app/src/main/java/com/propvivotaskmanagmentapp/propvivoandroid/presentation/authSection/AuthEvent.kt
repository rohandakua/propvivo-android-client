package com.propvivotaskmanagmentapp.propvivoandroid.presentation.authSection

import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Role

sealed class AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class RoleChanged(val role: Role) : AuthEvent()
    data object Submit : AuthEvent()
    data class ToggleAuthMode(val isSignIn: Boolean) : AuthEvent()
}
