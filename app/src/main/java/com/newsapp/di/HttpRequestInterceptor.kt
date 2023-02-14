package com.newsapp.di

import android.util.Log
import com.google.gson.Gson
import com.newsapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HttpRequestInterceptor : Interceptor {

    companion object {
        private const val TAG = "HttpRequestInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder().url(request.url()).build()
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "intercept: Request: $requestBuilder")
        }
        val response = chain.proceed(requestBuilder)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "intercept: Response: ${Gson().toJson(response.code())}")
        }
        return response
    }
}