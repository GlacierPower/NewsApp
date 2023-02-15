package com.newsapp.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newsapp.data.data_base.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDataBase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao

    companion object {

        private const val DATABASE_NAME = "news_database"
        private var DB_INSTANCE: NewsDataBase? = null

        fun getItemsDatabaseInstance(context: Context): NewsDataBase {
            return DB_INSTANCE ?: Room
                .databaseBuilder(
                    context.applicationContext,
                    NewsDataBase::class.java,
                    DATABASE_NAME
                )
                .addMigrations()
                .build()
                .also { DB_INSTANCE = it }
        }
    }
}