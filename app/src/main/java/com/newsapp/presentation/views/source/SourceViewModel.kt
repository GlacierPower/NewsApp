package com.newsapp.presentation.views.source

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.newsapp.App
import com.newsapp.data.model.SourceResponse
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
class SourceViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private var _source = MutableLiveData<Resources<SourceResponse>>()
    val source: LiveData<Resources<SourceResponse>> get() = _source

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
                    else -> toast(getApplication(), Constants.ERROR)
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
}