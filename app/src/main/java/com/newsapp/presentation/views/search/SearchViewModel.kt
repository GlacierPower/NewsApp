package com.newsapp.presentation.views.search

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.newsapp.R
import com.newsapp.data.model.NewsResponse
import com.newsapp.domain.news.NewsInteractor
import com.newsapp.util.Constants
import com.newsapp.util.InternetConnection
import com.newsapp.util.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor
) : ViewModel() {

    @Inject
    lateinit var internetConnection: InternetConnection
    private var _news = MutableLiveData<Resources<NewsResponse>>()
    val newsLD: LiveData<Resources<NewsResponse>> get() = _news

    private var _login = MutableLiveData<Boolean>()
    val login: LiveData<Boolean> get() = _login

    private var _loginNav = MutableLiveData<Int?>()
    val loginNav: LiveData<Int?> get() = _loginNav

    private var _progressBar = MutableLiveData<Int>()
    val progressBar: LiveData<Int> get() = _progressBar

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

    fun showProgressBar() {
        _progressBar.postValue(View.VISIBLE)
    }

    fun hideProgressBar(){
        _progressBar.postValue(View.GONE)
    }

    fun navigateToLogin() {
        _loginNav.value = R.navigation.auth_graph
    }

    fun isUserLoggedIn() {
        _login.value = FirebaseAuth.getInstance().currentUser != null
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
                if (internetConnection.isOnline()) {
                    val responce = newsInteractor.getSearchNews(query, searchPage)
                    _news.postValue(searchNewsResponse(responce))
                } else {
                    _news.postValue(Resources.Error(Constants.NO_CONNECTION))
                }
            } catch (exception: Exception) {
                when (exception) {

                    is IOException -> _news.postValue(Resources.Error(exception.message!!))
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