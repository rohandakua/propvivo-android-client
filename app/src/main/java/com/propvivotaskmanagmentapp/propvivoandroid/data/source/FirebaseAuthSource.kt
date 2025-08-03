package com.propvivotaskmanagmentapp.propvivoandroid.data.source

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.Role
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AuthRepositoryInterface
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
        Log.d(TAG, "=== FIRESTORE SIGN IN START ===")

        try {
            // Input validation
            if (email.isBlank() || password.isBlank()) {
                emit(Result.failure(IllegalArgumentException("Email and password cannot be empty")))
                return@flow
            }

            // Sign in with Firebase Auth
            val authResult = withTimeout(TIMEOUT_DURATION) {
                auth.signInWithEmailAndPassword(email, password).await()
            }

            val uid = authResult.user?.uid ?: throw IllegalStateException("User UID is null")
            Log.d(TAG, "Auth successful, UID: $uid")

            // Fetch user document from Firestore
            val userDocument = withTimeout(TIMEOUT_DURATION) {
                firestore.collection(USERS_COLLECTION)
                    .document(uid)
                    .get()
                    .await()
            }

            if (!userDocument.exists()) {
                throw IllegalStateException("User data not found in database")
            }

            // Convert Firestore document to User object
            val user = userDocument.toObject(User::class.java)
                ?: throw IllegalStateException("Failed to parse user data")

            // Validate role
            if (!isValidRole(user.role)) {
                throw IllegalStateException("Invalid user role: ${user.role}")
            }

            Log.d(TAG, "Sign in successful: $user")
            emit(Result.success(user))

        } catch (e: Exception) {
            Log.e(TAG, "Sign in failed", e)
            emit(Result.failure(e))
        }
    }

    override suspend fun register(
        email: String,
        name: String,
        password: String,
        role: String
    ): Flow<Result<User>> = flow {

        Log.d(TAG, "=== FIRESTORE REGISTRATION START ===")
        Log.d(TAG, "Email: $email, Name: $name, Role: $role")

        try {
            // Input validation
            if (email.isBlank() || name.isBlank() || password.isBlank() || role.isBlank()) {
                throw IllegalArgumentException("All fields are required")
            }

            if (!isValidRole(role)) {
                throw IllegalArgumentException("Invalid role: $role")
            }

            Log.d(TAG, "Creating Firebase Auth account...")
            // Create Firebase Auth account
            val authResult = withTimeout(TIMEOUT_DURATION) {
                auth.createUserWithEmailAndPassword(email, password).await()
            }

            val uid = authResult.user?.uid ?: throw IllegalStateException("User UID is null")
            Log.d(TAG, "Auth account created successfully, UID: $uid")

            // Create user object
            val user = User(uid, email, role, name)
            Log.d(TAG, "Saving user to Firestore: $user")

            try {
                // Save user to Firestore with automatic retry and better error handling
                withTimeout(TIMEOUT_DURATION) {
                    firestore.collection(USERS_COLLECTION)
                        .document(uid)
                        .set(user)
                        .await()
                }
                Log.d(TAG, "Firestore save completed successfully!")

                // Optionally update Firebase Auth profile
                authResult.user?.updateProfile(
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                )?.await()

            } catch (e: Exception) {
                Log.e(TAG, "Firestore save failed, cleaning up auth account...", e)

                // Clean up auth account since Firestore save failed
                try {
                    authResult.user?.delete()?.await()
                    Log.d(TAG, "Auth account cleanup successful")
                } catch (cleanupException: Exception) {
                    Log.e(TAG, "Failed to cleanup auth account", cleanupException)
                }

                throw e
            }

            Log.d(TAG, "Registration completed successfully")
            emit(Result.success(user))

        } catch (e: Exception) {
            Log.e(TAG, "Registration failed", e)

            // Provide specific error messages
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
            Log.d(TAG, "Sign out successful")
        } catch (e: Exception) {
            Log.e(TAG, "Sign out failed", e)
            throw e
        }
    }

    private fun isValidRole(role: String): Boolean {
        return try {
            Role.valueOf(role)
            true
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Invalid role: $role", e)
            false
        }
    }

    // Additional Firestore-specific methods for better user management
    suspend fun updateUserProfile(uid: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            withTimeout(TIMEOUT_DURATION) {
                firestore.collection(USERS_COLLECTION)
                    .document(uid)
                    .update(updates)
                    .await()
            }
            Log.d(TAG, "User profile updated successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update user profile", e)
            Result.failure(e)
        }
    }

    suspend fun getUsersByRole(role: String): Result<List<User>> {
        return try {
            val querySnapshot = withTimeout(TIMEOUT_DURATION) {
                firestore.collection(USERS_COLLECTION)
                    .whereEqualTo("role", role)
                    .get()
                    .await()
            }

            val users = querySnapshot.documents.mapNotNull { doc ->
                doc.toObject(User::class.java)
            }

            Log.d(TAG, "Retrieved ${users.size} users with role: $role")
            Result.success(users)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get users by role", e)
            Result.failure(e)
        }
    }

    suspend fun checkEmailExists(email: String): Result<Boolean> {
        return try {
            val querySnapshot = withTimeout(TIMEOUT_DURATION) {
                firestore.collection(USERS_COLLECTION)
                    .whereEqualTo("email", email)
                    .get()
                    .await()
            }

            Result.success(!querySnapshot.isEmpty)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to check email existence", e)
            Result.failure(e)
        }
    }
}