package com.newsapp.data.service

import com.newsapp.data.model.NewsResponse
import com.newsapp.util.Constants.KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String = "russia",
        @Query("page")
        pageCount:Int = 1,
        @Query("apikey")
        key: String = KEY,
        @Query("category")
        category:String? = null
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        query:String ,
        @Query("page")
        pageCount:Int = 1,
        @Query("apikey")
        key: String = KEY
    ): Response<NewsResponse>
}