package com.newsapp.data.dao

import androidx.room.*
import com.newsapp.data.data_base.FavoriteEntity
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

    @Query("DELETE FROM favoriteEntity")
    suspend fun deleteAllNews()

    @Query("DELETE FROM favoriteEntity WHERE title =:title")
    suspend fun deleteNewsByTitle(title: String)

    @Query("SELECT * FROM newsEntity WHERE title =:title")
    fun findNewsByTitle(title: String): NewsEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE) // ignore when conflict occurs (ignore items if same)
    fun insertFavoritesEntity(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favoriteEntity")
    fun getFavoriteEntities(): Flow<List<FavoriteEntity>>

}