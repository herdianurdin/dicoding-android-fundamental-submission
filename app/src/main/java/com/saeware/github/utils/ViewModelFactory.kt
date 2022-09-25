package com.saeware.github.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saeware.github.data.GithubRepository
import com.saeware.github.di.Injection
import com.saeware.github.ui.activity.DetailViewModel
import com.saeware.github.ui.activity.FavoriteViewModel
import com.saeware.github.ui.activity.MainViewModel
import com.saeware.github.ui.activity.SettingsViewModel
import com.saeware.github.ui.fragment.FollowersViewModel
import com.saeware.github.ui.fragment.FollowingViewModel

class ViewModelFactory private constructor(private val githubRepository: GithubRepository)
    : ViewModelProvider.NewInstanceFactory()
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(MainViewModel::class.java) ->
            MainViewModel(githubRepository) as T
        modelClass.isAssignableFrom(DetailViewModel::class.java) ->
            DetailViewModel(githubRepository) as T
        modelClass.isAssignableFrom(FollowersViewModel::class.java) ->
            FollowersViewModel(githubRepository) as T
        modelClass.isAssignableFrom(FollowingViewModel::class.java) ->
            FollowingViewModel(githubRepository) as T
        modelClass.isAssignableFrom(FavoriteViewModel::class.java) ->
            FavoriteViewModel(githubRepository) as T
        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(githubRepository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "application"
        )

        @Volatile
        private var instance: ViewModelFactory? = null
        fun getFactory(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(
                    context, context.dataStore
                ))
            }.also { instance = it }
    }
}