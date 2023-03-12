package com.newsapp.data.repositotyImpl

import com.newsapp.data.dao.NewsDao
import com.newsapp.data.data_base.FavoriteEntity
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import com.newsapp.data.service.ApiService
import com.newsapp.data.sharedpreferences.SettingDataStore
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.domain.news.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val apiService: ApiService,
    private val settingDataStore: SettingDataStore
//    private val store: DataStore
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

    override suspend fun insertData() {
        withContext(Dispatchers.IO) {
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

    override suspend fun insertCategory(category: String, page: Int) {
        withContext(Dispatchers.IO) {
            val response = apiService.getNewsByCategory(category = category, page = page)
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

    override suspend fun insertSearchNews(query: String, page: Int) {
        withContext(Dispatchers.IO) {
            val response = apiService.getSearchNews(query, page)
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


    override suspend fun deleteNewsByTitle(title: String) {
        return withContext(Dispatchers.IO) {
            newsDao.deleteNewsByTitle(title)
        }
    }

    override suspend fun deleteAllNews() {
        return withContext(Dispatchers.IO) {
            newsDao.deleteAllNews()
        }
    }

    override suspend fun findNewsByTitle(title: String): NewsEntity {
        return withContext(Dispatchers.IO) {
            newsDao.findNewsByTitle(title)
        }
    }

    override suspend fun insertToFavorite(newsEntity: NewsEntity) {
        return withContext(Dispatchers.IO) {
            newsDao.insertFavoritesEntity(
                FavoriteEntity(
                    newsEntity.id,
                    newsEntity.source,
                    newsEntity.author,
                    newsEntity.title,
                    newsEntity.description,
                    newsEntity.url,
                    newsEntity.urlToImage,
                    newsEntity.publishedAt,
                    newsEntity.content
                )
            )
        }
    }

    override suspend fun getFavorite(): Flow<List<FavoriteEntity>> {
        return withContext(Dispatchers.IO) {
            newsDao.getFavoriteEntities()
        }
    }

    override suspend fun setDarkMode(uiMode: UIMode) {
        withContext(Dispatchers.IO) {
            settingDataStore.setDarkMode(uiMode)
        }
    }

    override fun uIModeFlow(): Flow<UIMode> {
        return settingDataStore.uiModeFlow
    }

}
