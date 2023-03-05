package com.newsapp.presentation.views.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.data_base.FavoriteEntity
import com.newsapp.domain.NewsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor,
    application: Application
) : AndroidViewModel(application) {

    val news = flow<Flow<List<FavoriteEntity>>> {
        emit(newsInteractor.getFavorite())
    }

    fun deleteNews(title: String) {
        viewModelScope.launch {
            try {
                newsInteractor.deleteNewsByTitle(title)
            } catch (e: Exception) {
                Log.w("Delete news", e.message.toString())
            }

        }

    }


    fun deleteAllNews() {
        viewModelScope.launch {
            try {
                newsInteractor.deleteAllNews()
            } catch (e: Exception) {
                Log.w("Delete All News", e.message.toString())
            }

        }
    }
}