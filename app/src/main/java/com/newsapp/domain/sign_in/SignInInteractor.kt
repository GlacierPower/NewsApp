package com.newsapp.domain.sign_in

import javax.inject.Inject

class SignInInteractor @Inject constructor(
    private val signInRepository: SignInRepository
) {

    suspend fun isUserRegistered(): Boolean {
        return signInRepository.isUserRegistered()
    }

    suspend fun signIn(email: String, password: String, callback: (Boolean, String) -> Unit) {
        signInRepository.signIn(email, password, callback)
    }

    suspend fun sendPasswordResetEmail(email: String, callback: (Boolean, String) -> Unit) {
        signInRepository.sendPasswordResetEmail(email, callback)
    }

    suspend fun signOut() {
        signInRepository.signOut()
    }


}