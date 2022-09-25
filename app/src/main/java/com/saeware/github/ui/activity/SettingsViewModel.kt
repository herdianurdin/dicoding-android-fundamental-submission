package com.saeware.github.ui.activity

import androidx.lifecycle.*
import com.saeware.github.data.GithubRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    fun getDarkModeSetting(): LiveData<Boolean> =
        githubRepository.getDarkModeSetting().asLiveData()

    fun saveDarkModeSetting(state: Boolean) {
        viewModelScope.launch { githubRepository.saveDarkModeSetting(state) }
    }
}