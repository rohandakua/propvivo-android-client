package com.propvivotaskmanagmentapp.propvivoandroid.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Role
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AuthRepositoryInterface
import com.propvivotaskmanagmentapp.propvivoandroid.domain.util.FirebasePathConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRepositoryInterface {

    companion object {
        private const val TAG = "FirestoreAuthSource"
        private const val USERS_COLLECTION = "users"
        private const val TIMEOUT_DURATION = 15_000L // 15 seconds
    }

    override suspend fun signIn(email: String, password: String): Flow<Result<User>> = flow {
        try {
            if (email.isBlank() || password.isBlank()) {
                emit(Result.failure(IllegalArgumentException("Email and password cannot be empty")))
                return@flow
            }
            val authResult = withTimeout(TIMEOUT_DURATION) {
                auth.signInWithEmailAndPassword(email, password).await()
            }

            val uid = authResult.user?.uid ?: throw IllegalStateException("User UID is null")
            val userDocument = withTimeout(TIMEOUT_DURATION) {
                firestore.collection(USERS_COLLECTION)
                    .document(uid)
                    .get()
                    .await()
            }

            if (!userDocument.exists()) {
                throw IllegalStateException("User data not found in database")
            }
            val user = userDocument.toObject(User::class.java)
                ?: throw IllegalStateException("Failed to parse user data")
            if (!isValidRole(user.role)) {
                throw IllegalStateException("Invalid user role: ${user.role}")
            }
            emit(Result.success(user))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun register(
        email: String,
        name: String,
        password: String,
        role: String
    ): Flow<Result<User>> = flow {

        try {
            if (email.isBlank() || name.isBlank() || password.isBlank() || role.isBlank()) {
                throw IllegalArgumentException("All fields are required")
            }

            if (!isValidRole(role)) {
                throw IllegalArgumentException("Invalid role: $role")
            }

            Log.d(TAG, "Creating Firebase Auth account...")
            val authResult = withTimeout(TIMEOUT_DURATION) {
                auth.createUserWithEmailAndPassword(email, password).await()
            }

            val uid = authResult.user?.uid ?: throw IllegalStateException("User UID is null")
            val user = User(uid, email, role, name)

            try {
                withTimeout(TIMEOUT_DURATION) {
                    firestore.collection(USERS_COLLECTION)
                        .document(uid)
                        .set(user)
                        .await()
                }
                withTimeout(TIMEOUT_DURATION) {
                    firestore.collection(
                        when (role) {
                            Role.Employee.role -> FirebasePathConstants.EMPLOYEES
                            Role.Admin.role -> FirebasePathConstants.ADMINS
                            else-> FirebasePathConstants.SUPERVISORS
                        }

                    )
                        .document(uid)
                        .set(user)
                        .await()
                }
                authResult.user?.updateProfile(
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                )?.await()

            } catch (e: Exception) {
                try {
                    authResult.user?.delete()?.await()
                } catch (cleanupException: Exception) {
                }

                throw e
            }
            emit(Result.success(user))

        } catch (e: Exception) {
            Log.e(TAG, "Registration failed", e)
            val errorMessage = when (e) {
                is kotlinx.coroutines.TimeoutCancellationException -> {
                    "Registration timed out. Please check your internet connection and try again."
                }
                is com.google.firebase.firestore.FirebaseFirestoreException -> {
                    "Database error: ${e.message}. Please check your permissions."
                }
                is com.google.firebase.auth.FirebaseAuthException -> {
                    "Authentication error: ${e.message}"
                }
                else -> e.message ?: "Registration failed"
            }

            emit(Result.failure(Exception(errorMessage, e)))
        }
    }

    override fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            throw e
        }
    }

    private fun isValidRole(role: String): Boolean {
        return try {
            Role.valueOf(role)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }


}