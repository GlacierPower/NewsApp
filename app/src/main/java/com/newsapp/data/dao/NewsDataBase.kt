package com.newsapp.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.newsapp.data.data_base.FavoriteEntity
import com.newsapp.data.data_base.NewsEntity


@Database(entities = [NewsEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
abstract class NewsDataBase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao
    abstract fun getFavoriteDao(): FavoriteDao

    companion object {
        private const val DATABASE_NAME = "news_database"
        private var DB_INSTANCE: NewsDataBase? = null

        fun getNewsDatabaseInstance(context: Context): NewsDataBase {
            return DB_INSTANCE ?: Room
                .databaseBuilder(
                    context.applicationContext,
                    NewsDataBase::class.java,
                    DATABASE_NAME
                )
                .build()
                .also { DB_INSTANCE = it }
        }
    }
}