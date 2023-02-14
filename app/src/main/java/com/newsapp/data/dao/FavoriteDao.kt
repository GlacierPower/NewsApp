package com.newsapp.data.dao

import androidx.room.*
import com.newsapp.data.data_base.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteNews(favorite: FavoriteEntity): Long

    @Query("SELECT * FROM favoriteEntity LIMIT :limit")
    fun getFavouriteNews(limit: Int) : Flow<List<FavoriteEntity>?>

    @Delete
    suspend fun deleteFavouriteNews(favourite: FavoriteEntity) : Int

    @Query("SELECT isFavourite FROM favoriteEntity WHERE title = :title and category = :category")
    fun checkIfFavourite(title:String,category:String) : Flow<Int?>

    @Query("DELETE FROM favoriteEntity WHERE title = :title and category = :category and isFavourite = 1")
    suspend fun deleteFavourite(title:String,category:String) : Int
}