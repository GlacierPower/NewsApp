package com.newsapp.domain

import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import com.newsapp.model.NewsModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {

    suspend fun getNews(): Response<NewsResponse>

    suspend fun getBreakingNews(category: String, page: Int): Response<NewsResponse>

    suspend fun getSearchNews(query: String, page: Int): Response<NewsResponse>

    suspend fun getSourceNews(): Response<SourceResponse>

    suspend fun getData()
    suspend fun showData(): Flow<List<NewsEntity>>

    suspend fun deleteNewsByTitle(title: String)

    suspend fun deleteAllNews()
}