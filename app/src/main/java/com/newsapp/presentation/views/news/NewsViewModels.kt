package com.newsapp.presentation.views.news

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
class NewsViewModels @Inject constructor(
    private val newsInteractor: NewsInteractor
) : ViewModel() {

    @Inject
    lateinit var internetConnection: InternetConnection

    private var _connection = MutableLiveData<Boolean>()
    val connection: LiveData<Boolean> get() = _connection

    private var _news = MutableLiveData<Resources<NewsResponse>>()
    val newsLD: LiveData<Resources<NewsResponse>> get() = _news

    private var _userLoggedIn = MutableLiveData<Boolean>()
    val userLoggedIn: LiveData<Boolean> get() = _userLoggedIn

    private var _navLogin = MutableLiveData<Int?>()
    val navLogin: LiveData<Int?> get() = _navLogin


    private var _temp = MutableLiveData<Resources<NewsResponse>>()
    val temp: LiveData<Resources<NewsResponse>> get() = _temp


    fun navigateToLogin() {
        _navLogin.value = R.navigation.auth_graph
    }

    fun onFavClicked(title: String) {
        viewModelScope.launch {
            try {
                newsInteractor.insertToFavorite(title)
            } catch (e: Exception) {
                Log.w("News fav clicked", e.message.toString())
            }

        }
    }

    fun insertData() {
        viewModelScope.launch {
            try {
                newsInteractor.insertData()
            } catch (e: Exception) {
                Log.w("Insert data", e.message.toString())
            }
        }

    }

    fun isUserLoggedIn() {
        _userLoggedIn.value = FirebaseAuth.getInstance().currentUser != null
    }

    fun getNews() {
        _news.postValue(Resources.Loading())
        viewModelScope.launch {
            try {
                if (internetConnection.isOnline()) {
                    val response = newsInteractor.getNews()
                    _temp.postValue(Resources.Success(response.body()!!))
                    _news.postValue(newsResponse(response)!!)
                    _connection.value = false
                } else {
                    _connection.value = true
                    _news.postValue(Resources.Error(Constants.NO_CONNECTION))

                }
            } catch (exception: Exception) {
                when (exception) {
                    is IOException -> _news.postValue(Resources.Error(exception.message!!))
                }
            }
        }
    }


}