package com.newsapp.domain.sign_in

interface SignInRepository {
    suspend fun isUserRegistered(): Boolean

    suspend fun signIn(email: String, password: String, callback: (Boolean, String) -> Unit)

    suspend fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit)

    suspend fun signOut()
}