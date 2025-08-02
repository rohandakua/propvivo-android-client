package com.propvivotaskmanagmentapp.propvivoandroid.data.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.propvivotaskmanagmentapp.propvivoandroid.domain.enum.FirebasePathConstants
import com.propvivotaskmanagmentapp.propvivoandroid.domain.model.User
import com.propvivotaskmanagmentapp.propvivoandroid.domain.repository.interfaces.AuthRepositoryInterface
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: DatabaseReference
) : AuthRepositoryInterface {

    override suspend fun signIn(email: String, password: String): Flow<Result<User>> =
        callbackFlow {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    db.child(FirebasePathConstants.USERS).child(uid).get()
                        .addOnSuccessListener { snapshot ->
                            val role =
                                snapshot.child("role").getValue(String::class.java) ?: "unknown"
                            trySend(Result.success(User(uid, email, role)))
                            close()
                        }
                        .addOnFailureListener {
                            trySend(Result.failure(it))
                            close()
                        }
                }
                .addOnFailureListener {
                    trySend(Result.failure(it))
                    close()
                }

            awaitClose()
        }

    override suspend fun register(
        email: String,
        password: String,
        role: String
    ): Flow<Result<User>> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val user = User(uid, email, role)
                db.child(FirebasePathConstants.USERS).child(uid).setValue(user)
                    .addOnSuccessListener {
                        trySend(Result.success(user))
                        close()
                    }
                    .addOnFailureListener {
                        trySend(Result.failure(it))
                        close()
                    }
            }
            .addOnFailureListener {
                trySend(Result.failure(it))
                close()
            }

        awaitClose()
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}
