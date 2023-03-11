package com.newsapp.domain.sing_up

interface SignUpRepository {

    suspend fun createUser(email: String, password: String, callback: (Boolean, String) -> Unit)
}