package com.newsapp.domain

import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import retrofit2.Response

interface NewsRepository {

    suspend fun getNews(): Response<NewsResponse>

    suspend fun getBreakingNews(category: String, page: Int): Response<NewsResponse>

    suspend fun getSearchNews(query: String, page: Int): Response<NewsResponse>

    suspend fun getSourceNews(): Response<SourceResponse>
}