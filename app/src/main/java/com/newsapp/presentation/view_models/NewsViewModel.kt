package com.newsapp.presentation.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.newsapp.App
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.model.SourceResponse
import com.newsapp.data.sharedpreferences.DataStore
import com.newsapp.domain.NewsInteractor
import com.newsapp.util.Constants
import com.newsapp.util.Resources
import com.newsapp.util.hasInternetConnection
import com.newsapp.util.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private var _news = MutableLiveData<Resources<NewsResponse>>()
    val newsLD: LiveData<Resources<NewsResponse>> get() = _news

    private var _source = MutableLiveData<Resources<SourceResponse>>()
    val source: LiveData<Resources<SourceResponse>> get() = _source

    private var _temp = MutableLiveData<Resources<NewsResponse>>()
    val temp: LiveData<Resources<NewsResponse>> get() = _temp

    private var breakingNews = 1
    var searchPage = 1
    var searchResponse: NewsResponse? = null


    private val store = DataStore(application)
    val getTheme = store.uiMode

    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            try {
                store.saveToDataStore(isDarkMode)
            } catch (e: Exception) {
                Log.e("Save theme", "failed")
            }
        }
    }

    init {
        viewModelScope.launch {
            getNews()
        }
    }

    suspend fun getNews() {
        _news.postValue(Resources.Loading())
        try {
            if (hasInternetConnection<App>()) {
                val response = newsInteractor.getNews()
                _temp.postValue(Resources.Success(response.body()!!))
                _news.postValue(newsResponse(response))
            } else {
                _news.postValue(Resources.Error(Constants.NO_CONNECTION))
                toast(getApplication(), Constants.NO_CONNECTION)
            }
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> _news.postValue(Resources.Error(exception.message!!))
                else -> Log.e("Error", exception.toString())
            }
        }

    }

    suspend fun getBreakingNews(category: String = Constants.categories.first()) {
        _news.postValue(Resources.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection<App>()) {
                    val response = newsInteractor.getBreakingNews(category, breakingNews)
                    _news.postValue(newsResponse(response))
                } else {
                    _news.postValue(Resources.Error(Constants.NO_CONNECTION))
                    toast(getApplication(), Constants.NO_CONNECTION)
                }
            } catch (exception: Exception) {
                when (exception) {
                    is IOException -> _news.postValue(Resources.Error(exception.message!!))
                    else -> Log.e("Error", exception.toString())
                }

            }
        }

    }


    suspend fun getSearchNews(query: String) {
        _news.postValue(Resources.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection<App>()) {
                    val responce = newsInteractor.getSearchNews(query, searchPage)
                    _news.postValue(searchNewsResponse(responce))
                } else {
                    _news.postValue(Resources.Error(Constants.NO_CONNECTION))
                    toast(getApplication(), Constants.NO_CONNECTION)
                }
            } catch (throwable: Throwable) {
                when (throwable) {

                    is IOException -> _news.postValue(Resources.Error(throwable.message!!))
                    else -> _news.postValue(Resources.Error(throwable.message!!))
                }
            }
        }
    }


    fun searchNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                searchPage++
                if (searchResponse == null) {
                    searchResponse = result
                } else {
                    val oldNews = searchResponse?.articles
                    val newNews = result.articles

                    oldNews?.addAll(newNews)
                }
                return Resources.Success(searchResponse ?: result)
            }
        }
        return Resources.Error(response.message())
    }


    suspend fun getSourceNews() {
        _source.postValue(Resources.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection<App>()) {
                    val response = newsInteractor.getSourceNews()
                    _source.postValue(sourceResponse(response))
                } else {
                    _source.postValue(Resources.Error(Constants.NO_CONNECTION))
                    toast(getApplication(), Constants.NO_CONNECTION)
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _source.postValue(Resources.Error(throwable.message!!))
                    else -> _source.postValue(Resources.Error(throwable.message!!))
                }
            }
        }
    }


    private fun sourceResponse(response: Response<SourceResponse>): Resources<SourceResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resources.Success(result)
            }
        }
        return Resources.Error(response.message())
    }


    private fun newsResponse(response: Response<NewsResponse>): Resources<NewsResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resources.Success(result)
            }
        }
        return Resources.Error(response.message())
    }
}