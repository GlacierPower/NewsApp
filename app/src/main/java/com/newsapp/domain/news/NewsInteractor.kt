package com.newsapp.domain.news

import com.newsapp.data.data_base.FavoriteEntity
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import com.newsapp.data.sharedpreferences.UIMode
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

    suspend fun insertData() {
        newsRepository.insertData()
    }

    suspend fun insertCategory(category: String, page: Int) {
        newsRepository.insertCategory(category, page)
    }

    suspend fun insertSearchNews(query: String, page: Int) {
        newsRepository.insertSearchNews(query, page)
    }

    suspend fun deleteNewsByTitle(title: String) {
        newsRepository.deleteNewsByTitle(title)
    }

    suspend fun deleteAllNews() {
        newsRepository.deleteAllNews()
    }

    suspend fun findNewsByTitle(title: String): NewsEntity {
        return newsRepository.findNewsByTitle(title)
    }

    suspend fun insertToFavorite(title: String) {
        val foundItem = newsRepository.findNewsByTitle(title)
        return newsRepository.insertToFavorite(foundItem)
    }

    suspend fun getFavorite(): Flow<List<FavoriteEntity>> {
        return newsRepository.getFavorite()
    }

}