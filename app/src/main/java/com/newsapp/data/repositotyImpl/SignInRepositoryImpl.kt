package com.newsapp.data.repositotyImpl

import com.google.firebase.auth.FirebaseAuth
import com.newsapp.domain.sign_in.SignInRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private var firebaseAuth: FirebaseAuth
) : SignInRepository {

    override suspend fun isUserRegistered(): Boolean {
        return withContext(Dispatchers.IO) {
            firebaseAuth.currentUser != null
        }
    }

    override suspend fun signIn(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    task.exception?.message?.let { msg ->
                        callback.invoke(task.isSuccessful, msg)
                    } ?: callback.invoke(task.isSuccessful, "")
                }
        }
    }

    override suspend fun sendPasswordResetEmail(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    task.exception?.message?.let { msg ->
                        callback.invoke(task.isSuccessful, msg)
                    } ?: callback.invoke(task.isSuccessful, "")
                }
        }
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
            firebaseAuth.signOut()
        }
    }
}