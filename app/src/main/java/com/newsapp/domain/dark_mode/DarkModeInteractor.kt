package com.newsapp.domain.dark_mode

import com.newsapp.data.sharedpreferences.UIMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DarkModeInteractor @Inject constructor(
    private val darkModeRepository: DarkModeRepository
    ) {

    suspend fun setDarkMode(uiMode: UIMode) {
        darkModeRepository.setDarkMode(uiMode)
    }

    fun uIModeFlow(): Flow<UIMode> {
        return darkModeRepository.uIModeFlow()
    }
}