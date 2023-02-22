package com.newsapp.di

import com.newsapp.domain.NewsInteractor
import com.newsapp.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun provideNewsInteractor(
        newsRepository: NewsRepository
    ):NewsInteractor{
        return  NewsInteractor(newsRepository)
    }
}