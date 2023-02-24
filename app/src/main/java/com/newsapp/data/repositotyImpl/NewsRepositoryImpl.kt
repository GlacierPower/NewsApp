package com.newsapp.data.repositotyImpl

import com.newsapp.data.dao.NewsDao
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import com.newsapp.data.service.ApiService
import com.newsapp.domain.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val apiService: ApiService
) : NewsRepository {
    override suspend fun getNews(): Response<NewsResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getNews()
        }
    }

    override suspend fun getBreakingNews(category: String, page: Int): Response<NewsResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getNewsByCategory(category = category, page = page)
        }
    }

    override suspend fun getSearchNews(query: String, page: Int): Response<NewsResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getSearchNews(searchQuery = query, page = page)
        }
    }

    override suspend fun getSourceNews(): Response<SourceResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getSourcesNews()
        }
    }

    override suspend fun getData() {
        withContext(Dispatchers.IO) {
            newsDao.doesNewsEntityExist().collect {
                if (!it) {
                    val response = apiService.getNews()
                    response.body()?.articles.let { listNews ->
                        listNews?.map { news ->
                            val newsEntity = NewsEntity(
                                news.id,
                                news.source,
                                news.author,
                                news.title,
                                news.description,
                                news.url,
                                news.urlToImage,
                                news.publishedAt,
                                news.content
                            )
                            newsDao.insertToNewsEntity(newsEntity)
                        }
                    }
                }
            }
        }
    }

    override suspend fun showData(): Flow<List<NewsEntity>> {
        return withContext(Dispatchers.IO) {
            newsDao.getNewsEntities()
        }
    }

    override suspend fun deleteNewsByTitle(title: String) {
        return withContext(Dispatchers.IO) {
            newsDao.deleteItemEntityByDescription(title)
        }
    }

    override suspend fun deleteAllNews() {
        newsDao.deleteAllNews()
    }
}
