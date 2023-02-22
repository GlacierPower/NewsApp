package com.newsapp.data.service

import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import com.newsapp.util.Constants.KEY
import com.newsapp.util.Constants.categories
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode: String = "us",
        @Query("apiKey")
        apiKey: String = KEY
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getNewsByCategory(
        @Query("country") country: String = "us",
        @Query("category") category: String = categories.first(),
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = KEY,
    ): Response<NewsResponse>

    @GET("v2/sources")
    suspend fun getSourcesNews(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = KEY,
    ): Response<SourceResponse>

    @GET("v2/everything")
    suspend fun getSearchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        page: Int = 1,
        @Query("apiKey")
        apiKey: String = KEY
    ): Response<NewsResponse>
}