package com.newsapp.presentation.views.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.newsapp.App
import com.newsapp.data.model.NewsResponse
import com.newsapp.domain.NewsInteractor
import com.newsapp.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private var _news = MutableLiveData<Resources<NewsResponse>>()
    val newsLD: LiveData<Resources<NewsResponse>> get() = _news

    private var breakingNews = 1

    init {
        viewModelScope.launch {
            getBreakingNews()
        }
    }

    suspend fun getBreakingNews(category: String = Constants.categories.first()) {
        _news.postValue(Resources.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection<App>()) {
                    val response = newsInteractor.getBreakingNews(category, breakingNews)
                    _news.postValue(newsResponse(response)!!)
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
}