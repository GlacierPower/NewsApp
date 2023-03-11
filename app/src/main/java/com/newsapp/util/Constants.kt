package com.newsapp.util

import android.util.Patterns

object Constants {
    const val KEY = "9a78890d27a14db5928519eb358d0fd4"
    const val BASE_URL = "https://newsapi.org"
    const val DELAY = 1000L
    const val NO_CONNECTION = "No internet connection!"
    const val ERROR = "Something went wrong! Please try again later"
    const val PAGE_SIZE = 20
    const val FAVORITE = "News was added to Favorite"

    val categories = listOf(
        "Business",
        "Entertainment",
        "Health",
        "Science",
        "Sports",
        "Technology"
    )
    fun String.isEmailValid() = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}