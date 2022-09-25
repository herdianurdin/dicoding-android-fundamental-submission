package com.saeware.github.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeware.github.data.GithubRepository
import com.saeware.github.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val githubRepository: GithubRepository): ViewModel() {
    private val _favoriteUsers = MutableLiveData<List<UserEntity>>()
    val favoriteUsers: LiveData<List<UserEntity>> = _favoriteUsers

    init { getFavoriteUsers() }

    private fun getFavoriteUsers() {
        viewModelScope.launch {
            githubRepository.getFavoriteUsers().collect { _favoriteUsers.value = it }
        }
    }
}