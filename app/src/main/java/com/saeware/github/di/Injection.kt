package com.saeware.github.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.saeware.github.data.AppPreferences
import com.saeware.github.data.GithubRepository
import com.saeware.github.data.local.room.UserDatabase
import com.saeware.github.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): GithubRepository {
        val apiService = ApiConfig.getApiService()
        val userDao = UserDatabase.getDatabase(context).userDao()
        val appPreferences = AppPreferences.getPreferences(dataStore)

        return GithubRepository.getRepository(apiService, userDao, appPreferences)
    }
}