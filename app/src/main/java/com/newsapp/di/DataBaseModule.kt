package com.newsapp.di

import android.content.Context
import com.newsapp.data.dao.NewsDao
import com.newsapp.data.dao.NewsDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    fun provideNewsDataBase(context: Context): NewsDataBase {
        return NewsDataBase.getItemsDatabaseInstance(context)
    }

    @Provides
    fun provideNewsDao(newsDataBase: NewsDataBase): NewsDao {
        return newsDataBase.getNewsDao()
    }
}