package com.newsapp.util

import com.newsapp.data.model.NewsResponse
import retrofit2.Response

fun newsResponse(response: Response<NewsResponse>): Resources<NewsResponse>? {
    if (response.isSuccessful) {
        response.body()?.let { result ->
            return Resources.Success(result)
        }
    }
    return Resources.Error(response.message())
}