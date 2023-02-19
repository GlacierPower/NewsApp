package com.newsapp.data.dao

import androidx.room.*
import com.newsapp.data.data_base.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(news: NewsEntity): Long

    @Query("SELECT * FROM newsEntity ORDER BY id DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM newsEntity WHERE title= :q")
    suspend fun getNewsByTitle(q: String): NewsEntity

    @Delete
    suspend fun deleteNews(newsEntity: NewsEntity)

    @Query("DELETE FROM newsEntity")
    suspend fun deleteAllNews()

}