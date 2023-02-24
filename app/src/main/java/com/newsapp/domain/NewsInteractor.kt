package com.newsapp.domain

import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import com.newsapp.model.NewsModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class NewsInteractor @Inject constructor(private val newsRepository: NewsRepository) {

    suspend fun getNews(): Response<NewsResponse> {
        return newsRepository.getNews()
    }

    suspend fun getBreakingNews(category: String, page: Int): Response<NewsResponse> {
        return newsRepository.getBreakingNews(category, page)
    }

    suspend fun getSearchNews(query: String, page: Int): Response<NewsResponse> {
        return newsRepository.getSearchNews(query, page)
    }

    suspend fun getSourceNews(): Response<SourceResponse> {
        return newsRepository.getSourceNews()
    }

    suspend fun getData() {
        newsRepository.getData()
    }

    suspend fun showData(): Flow<List<NewsEntity>> {
        return newsRepository.showData()
    }

    suspend fun deleteNewsByTitle(title: String) {
        newsRepository.deleteNewsByTitle(title)
    }

    suspend fun deleteAllNews() {
        newsRepository.deleteAllNews()
    }
}