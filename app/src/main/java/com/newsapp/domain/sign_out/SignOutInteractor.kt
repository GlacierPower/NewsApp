package com.newsapp.domain.sign_out

import com.newsapp.domain.sign_in.SignInRepository
import javax.inject.Inject

class SignOutInteractor @Inject constructor(
    private val signInRepository: SignInRepository
) {
    suspend operator fun invoke() {
        signInRepository.signOut()
    }
}