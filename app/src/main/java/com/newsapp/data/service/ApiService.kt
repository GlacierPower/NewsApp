package com.newsapp.data.service

import com.newsapp.model.NewsModel
import com.newsapp.utils.Constants.API_KEY
import com.newsapp.utils.Constants.ITEM_COUNT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHedLinesWS(
        @Query("country") country: String? = "ua",
        @Query("apiKey") apiKey: String? = API_KEY,
        @Query("category") category: String? = "",
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = ITEM_COUNT
    ): Response<NewsModel>
}