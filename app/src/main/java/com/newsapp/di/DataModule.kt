package com.newsapp.di

import com.newsapp.data.repositotyImpl.DarkModeRepositoryImpl
import com.newsapp.data.repositotyImpl.NewsRepositoryImpl
import com.newsapp.data.repositotyImpl.SignInRepositoryImpl
import com.newsapp.data.repositotyImpl.SignUpRepositoryImpl
import com.newsapp.data.service.ApiService
import com.newsapp.domain.dark_mode.DarkModeRepository
import com.newsapp.domain.news.NewsRepository
import com.newsapp.domain.sign_in.SignInRepository
import com.newsapp.domain.sing_up.SignUpRepository
import com.newsapp.util.Constants.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindDarkModeRepository(
        darkModeRepositoryImpl: DarkModeRepositoryImpl
    ): DarkModeRepository

    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    abstract fun bindSignInRepository(
        signInRepositoryImpl: SignInRepositoryImpl
    ): SignInRepository

    @Binds
    abstract fun bindSignUpRepository(
        signUpRepositoryImpl: SignUpRepositoryImpl
    ): SignUpRepository

    companion object {
        @Provides
        fun provideApiService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)

        @Provides
        fun providesBaseUrl(): String {
            return BASE_URL
        }


        @Provides
        fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @Provides
        fun provideOkHttpClientClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
            return okHttpClient.build()
        }

        @Provides
        fun provideRetrofit(
            baseUrl: String,
            client: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

    }

}