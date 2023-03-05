package com.newsapp.presentation.views.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.newsapp.App
import com.newsapp.data.model.NewsResponse
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
class SearchViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private var _news = MutableLiveData<Resources<NewsResponse>>()
    val newsLD: LiveData<Resources<NewsResponse>> get() = _news

    var searchPage = 1
    var searchResponse: NewsResponse? = null

    fun onFavClicked(title: String) {
        viewModelScope.launch {
            try {
                newsInteractor.insertToFavorite(title)
            } catch (e: Exception) {
                Log.w("Search fav clicked", e.message.toString())
            }

        }
    }

    fun insertSearchNews(query: String) {
        viewModelScope.launch {
            try {
                newsInteractor.insertSearchNews(query, searchPage)
            } catch (e: Exception) {
                Log.w("Insert search news", e.message.toString())
            }

        }
    }

     fun getSearchNews(query: String) {
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
            } catch (exception: Exception) {
                when (exception) {

                    is IOException -> _news.postValue(Resources.Error(exception.message!!))
                    else -> toast(getApplication(), Constants.ERROR)
                }
            }
        }
    }

    private fun searchNewsResponse(response: Response<NewsResponse>): Resources<NewsResponse> {
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
}