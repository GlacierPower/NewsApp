package com.newsapp.domain.dark_mode

import com.newsapp.data.sharedpreferences.UIMode
import kotlinx.coroutines.flow.Flow

interface DarkModeRepository {

    suspend fun setDarkMode(uiMode: UIMode)

    fun uIModeFlow(): Flow<UIMode>
}