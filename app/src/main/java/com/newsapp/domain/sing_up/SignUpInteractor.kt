package com.newsapp.domain.sing_up

import javax.inject.Inject

class SignUpInteractor @Inject constructor(
    private val signUpRepository: SignUpRepository
) {

    suspend fun createUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        signUpRepository.createUser(email, password, callback)
    }
}