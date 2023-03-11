package com.newsapp.data.repositotyImpl

import com.google.firebase.auth.FirebaseAuth
import com.newsapp.domain.sing_up.SignUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private var firebaseAuth: FirebaseAuth
) : SignUpRepository {
    override suspend fun createUser(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    task.exception?.message?.let { msg ->
                        callback.invoke(task.isSuccessful, msg)
                    } ?: callback.invoke(task.isSuccessful, "")
                    }
        }
    }
}