package com.saeware.github.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getDarkModeSetting(): Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[DARK_MODE_KEY] ?: false }

    suspend fun saveDarkModeSetting(state: Boolean) {
        dataStore.edit { preferences -> preferences[DARK_MODE_KEY] = state }
    }

    companion object {
        private const val SETTING_KEY_DARK_MODE = "dark_mode_setting"
        private val DARK_MODE_KEY = booleanPreferencesKey(SETTING_KEY_DARK_MODE)

        @Volatile
        private var instance: AppPreferences? = null
        fun getPreferences(dataStore: DataStore<Preferences>): AppPreferences =
            instance ?: synchronized(this) {
                AppPreferences(dataStore).also { instance = it }
            }
    }
}