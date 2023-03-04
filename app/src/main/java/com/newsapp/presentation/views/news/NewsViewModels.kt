package com.newsapp.presentation.views.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.newsapp.App
import com.newsapp.data.model.NewsResponse
import com.newsapp.data.sharedpreferences.DataStore
import com.newsapp.domain.NewsInteractor
import com.newsapp.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModels @Inject constructor(
    private val newsInteractor: NewsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private var _news = MutableLiveData<Resources<NewsResponse>>()
    val newsLD: LiveData<Resources<NewsResponse>> get() = _news

    private var _temp = MutableLiveData<Resources<NewsResponse>>()
    val temp: LiveData<Resources<NewsResponse>> get() = _temp

    init {
        viewModelScope.launch {
            //getNews()
        }

    }

    suspend fun getNews() {
        _news.postValue(Resources.Loading())
        try {
            if (hasInternetConnection<App>()) {
                val response = newsInteractor.getNews()
                _temp.postValue(Resources.Success(response.body()!!))
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

}