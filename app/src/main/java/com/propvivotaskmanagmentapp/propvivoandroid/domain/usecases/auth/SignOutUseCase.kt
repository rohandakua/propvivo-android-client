package com.propvivotaskmanagmentapp.propvivoandroid.domain.usecases.auth

import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.flow.Flow

class SignOutUseCase(private val repository: AuthRepositoryInterface) {
    fun invoke() {
        return repository.signOut()
    }
}