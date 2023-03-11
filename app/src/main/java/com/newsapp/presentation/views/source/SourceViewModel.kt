package com.newsapp.presentation.views.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.model.SourceResponse
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
class SourceViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor
) : ViewModel() {
    @Inject
    lateinit var internetConnection: InternetConnection

    private var _source = MutableLiveData<Resources<SourceResponse>>()
    val source: LiveData<Resources<SourceResponse>> get() = _source

    private var _connection = MutableLiveData<Boolean>()
    val connection: LiveData<Boolean> get() = _connection

    fun getSourceNews() {
        _source.postValue(Resources.Loading())
        viewModelScope.launch {
            try {
                if (internetConnection.isOnline()) {
                    val response = newsInteractor.getSourceNews()
                    _source.postValue(sourceResponse(response))
                    _connection.value = false
                } else {
                    _source.postValue(Resources.Error(Constants.NO_CONNECTION))
                    _connection.value = true
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _source.postValue(Resources.Error(throwable.message!!))
                }
            }
        }

    }

    private fun sourceResponse(response: Response<SourceResponse>): Resources<SourceResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resources.Success(result)
            }
        }
        return Resources.Error(response.message())
    }
}