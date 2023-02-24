package com.newsapp.presentation.views.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.data_base.NewsEntity
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

    val items = flow<Flow<List<NewsEntity>>> {
        emit(newsInteractor.showData())
    }


    fun deleteNews(title: String) {
        viewModelScope.launch {
            newsInteractor.deleteNewsByTitle(title)
        }

    }

    suspend fun getData() {
        newsInteractor.getData()
    }

    suspend fun deleteAllNews() {
        newsInteractor.deleteAllNews()
    }
}