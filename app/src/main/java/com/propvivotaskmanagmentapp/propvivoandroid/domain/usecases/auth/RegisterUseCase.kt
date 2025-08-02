package com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth

import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Role
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.flow.Flow

class RegisterUseCase(private val repository: AuthRepositoryInterface) {
    suspend operator fun invoke(email: String, password: String , role: Role): Flow<Result<User>> {
        return repository.register(email, password, role.role)
    }
}