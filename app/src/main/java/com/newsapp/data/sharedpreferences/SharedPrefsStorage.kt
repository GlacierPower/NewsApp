package com.newsapp.data.sharedpreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

abstract class PrefsDataStore(context: Context, fileName: String) {
    internal val dataStore: DataStore<Preferences> = context.createDataStore(fileName)
}

class DataStore(context: Context) : PrefsDataStore(context, PREF_FILE_UI_MODE), UIModeImpl {


    override val uiMode: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val uiMode = preferences[UI_MODE_KEY] ?: false
            uiMode
        }

     val readCountry: Flow<String>
        get() = dataStore.data.map { preferences ->
            val country = preferences[COUNTRY_KEY] ?: "us"
            country
        }

    override suspend fun saveToDataStore(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[UI_MODE_KEY] = isNightMode
        }

    }

    override suspend fun saveCountry(country: String) {
        dataStore.edit { preference ->
            preference[COUNTRY_KEY] = country
        }
    }

    companion object {
        private const val PREF_FILE_UI_MODE = "ui_mode_preference"
        private val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
        private val COUNTRY_KEY = stringPreferencesKey("country")
    }


}

@Singleton
interface UIModeImpl {
    val uiMode: Flow<Boolean>
    suspend fun saveToDataStore(isNightMode: Boolean)

    suspend fun saveCountry(country: String)
}