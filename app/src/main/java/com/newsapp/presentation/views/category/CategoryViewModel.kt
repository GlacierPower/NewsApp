package com.newsapp.presentation.views.category

import android.util.Log
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
import com.newsapp.util.newsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor
) : ViewModel() {

    @Inject
    lateinit var internetConnection: InternetConnection

    private var _news = MutableLiveData<Resources<NewsResponse>>()
    val newsLD: LiveData<Resources<NewsResponse>> get() = _news

    private var _connection = MutableLiveData<Boolean>()
    val connection: LiveData<Boolean> get() = _connection

    private var _loginNav = MutableLiveData<Int?>()
    val loginNav: LiveData<Int?> get() = _loginNav

    private var _userLoggedIn = MutableLiveData<Boolean>()
    val userLoggedIn: LiveData<Boolean> get() = _userLoggedIn

    private var breakingNews = 1

    init {
        viewModelScope.launch {
            getBreakingNews()
        }
    }


    fun navigateToLogin() {
        _loginNav.value = R.navigation.auth_graph
    }

    fun isUserLoggedIn() {
        _userLoggedIn.value = FirebaseAuth.getInstance().currentUser != null
    }

    fun onFavClicked(title: String) {
        viewModelScope.launch {
            try {
                newsInteractor.insertToFavorite(title)
            } catch (e: Exception) {
                Log.w("Insert to Favorite", e.message.toString())
            }

        }
    }

    fun insertCategory(category: String = Constants.categories.first()) {
        viewModelScope.launch {
            try {
                newsInteractor.insertCategory(category, breakingNews)
            } catch (e: Exception) {
                Log.w("InsertCategory", e.message.toString())
            }

        }
    }

    fun getBreakingNews(category: String = Constants.categories.first()) {
        _news.postValue(Resources.Loading())
        viewModelScope.launch {
            try {
                if (internetConnection.isOnline()) {
                    val response = newsInteractor.getBreakingNews(category, breakingNews)
                    _news.postValue(newsResponse(response)!!)
                    _connection.value = false
                } else {
                    _news.postValue(Resources.Error(Constants.NO_CONNECTION))
                    _connection.value = true
                }
            } catch (exception: Exception) {
                when (exception) {
                    is IOException -> _news.postValue(Resources.Error(exception.message!!))
                }

            }
        }

    }
}