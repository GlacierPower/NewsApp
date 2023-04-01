package com.newsapp.data.repositotyImpl

import com.newsapp.data.sharedpreferences.SettingDataStore
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.domain.dark_mode.DarkModeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DarkModeRepositoryImpl @Inject constructor(
    private val settingDataStore: SettingDataStore
):DarkModeRepository {
    override suspend fun setDarkMode(uiMode: UIMode) {
        withContext(Dispatchers.IO) {
            settingDataStore.setDarkMode(uiMode)
        }
    }

    override fun uIModeFlow(): Flow<UIMode> {
        return settingDataStore.uiModeFlow
    }
}