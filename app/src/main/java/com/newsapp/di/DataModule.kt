package com.newsapp.di

import android.content.Context
import com.newsapp.data.service.ApiService
import com.newsapp.util.Constants.BASE_URL
import com.newsapp.util.Constants.ERROR
import com.newsapp.util.IConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = (5 * 1024 * 1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }

    @Provides
    fun provideOkHttpClient(cache: Cache, connectivity: IConnectivity): OkHttpClient {
        return OkHttpClient
            .Builder()
            .cache(cache)
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (connectivity.hasInternetConnection()) {
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                } else {
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                }
                val response = chain.proceed(request)
                if (response.code == 500) {
                    throw Exception(ERROR)
                } else if (response.code == 404) {
                    throw Exception(ERROR)
                }
                response
            }
            .connectTimeout(2000, TimeUnit.MILLISECONDS)
            .readTimeout(2000, TimeUnit.MILLISECONDS)
            .build()
    }
}