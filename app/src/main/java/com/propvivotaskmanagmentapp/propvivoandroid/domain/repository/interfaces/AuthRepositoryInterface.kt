package com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepositoryInterface {
    suspend fun signIn(email: String, password: String): Flow<Result<User>>
    suspend fun register(email: String,name: String, password: String, role: String): Flow<Result<User>>
    fun signOut()
}