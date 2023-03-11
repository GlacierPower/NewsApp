package com.newsapp.di

import com.newsapp.domain.news.NewsInteractor
import com.newsapp.domain.news.NewsRepository
import com.newsapp.domain.sign_in.SignInInteractor
import com.newsapp.domain.sign_in.SignInRepository
import com.newsapp.domain.sing_up.SignUpInteractor
import com.newsapp.domain.sing_up.SignUpRepository
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
    ): NewsInteractor {
        return NewsInteractor(newsRepository)
    }

    @Provides
    fun provideSignInInteractor(
        signInRepository: SignInRepository
    ): SignInInteractor {
        return SignInInteractor(signInRepository)
    }

    @Provides
    fun provideSignUpInteractor(
        signUpRepository: SignUpRepository
    ): SignUpInteractor {
        return SignUpInteractor(signUpRepository)
    }
}