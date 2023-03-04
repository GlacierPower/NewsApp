package com.newsapp.presentation.views.setting

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.sharedpreferences.DataStore
import com.newsapp.domain.NewsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor,
    application: Application
) : AndroidViewModel(application) {

    private val store = DataStore(application)
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