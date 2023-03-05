package com.newsapp.domain

import com.newsapp.data.data_base.FavoriteEntity
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {

    suspend fun getNews(): Response<NewsResponse>

    suspend fun getBreakingNews(category: String, page: Int): Response<NewsResponse>

    suspend fun getSearchNews(query: String, page: Int): Response<NewsResponse>

    suspend fun getSourceNews(): Response<SourceResponse>

    suspend fun insertData()

    suspend fun insertCategory(category: String, page: Int)

    suspend fun insertSearchNews(query: String, page: Int)

    suspend fun deleteNewsByTitle(title: String)

    suspend fun deleteAllNews()

    suspend fun findNewsByTitle(title: String): NewsEntity

    suspend fun insertToFavorite(newsEntity: NewsEntity)

    suspend fun getFavorite(): Flow<List<FavoriteEntity>>

}