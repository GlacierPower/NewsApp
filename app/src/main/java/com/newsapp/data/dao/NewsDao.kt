package com.newsapp.data.dao

import androidx.room.*
import com.newsapp.data.data_base.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsLis(newsList: List<NewsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity)

    @Query("SELECT * FROM news_entity WHERE page= :page")
    fun getNews(page: Int): Flow<List<NewsEntity>>

    @Delete
    suspend fun deleteNewsFromDB(newsEntry: List<NewsEntity>)
}