package com.newsapp.data.sharedpreferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

abstract class PrefsDataStore(context: Context, fileName: String) {
    internal val dataStore: DataStore<Preferences> = context.createDataStore(fileName)
}

class DataStore @Inject constructor(context: Context) : PrefsDataStore(context, PREF_FILE_UI_MODE) {


    suspend fun saveUiMode(isDarkMode:Boolean){
        dataStore.edit { preference ->
            preference[UI_MODE_KEY] = isDarkMode
            Log.w("save data store", "success")
        }
    }

    val readUiModeFromDataStore: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }else {
                throw exception
            }
        }
        .map { preference ->
            Log.w("get data store ", "success")
            val uiMode = preference[UI_MODE_KEY] ?: false
            uiMode

        }

    companion object {
        private const val PREF_FILE_UI_MODE = "ui_mode_preference"
        private val UI_MODE_KEY = booleanPreferencesKey("ui_mode")
        private val COUNTRY_KEY = stringPreferencesKey("country")
    }


}
