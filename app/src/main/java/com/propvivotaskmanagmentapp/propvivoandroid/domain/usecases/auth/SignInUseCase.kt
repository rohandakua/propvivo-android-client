package com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.flow.Flow

class SignInUseCase(private val repository: AuthRepositoryInterface) {
    suspend operator fun invoke(email: String, password: String): Flow<Result<User>> {
        return repository.signIn(email, password)
    }
}