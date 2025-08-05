package com.propvivotaskmanagmentapp.propvivoandroid.presentation.authSection

import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Role

data class AuthState(
    val isLoading: Boolean = false,
    val isSignIn: Boolean = true,
    val email: String = "",
    val password: String = "",
    val role: Role = Role.Employee,
    val errorMessage: String? = null,
    val name: String = ""
)
