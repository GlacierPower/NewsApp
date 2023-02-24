package com.newsapp.data.dao

import androidx.room.*
import com.newsapp.data.data_base.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToNewsEntity(news: NewsEntity)

    @Query("SELECT (SELECT COUNT(*) FROM newsEntity) !=0")
    fun doesNewsEntityExist(): Flow<Boolean>

    @Query("SELECT * FROM newsEntity")
    fun getNewsEntities(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM newsEntity ORDER BY id DESC")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM newsEntity WHERE title= :q")
    suspend fun getNewsByTitle(q: String): NewsEntity

    @Query("DELETE FROM newsEntity")
    suspend fun deleteAllNews()

    @Query("DELETE FROM newsEntity WHERE title =:title")
    suspend fun deleteItemEntityByDescription(title: String)

}