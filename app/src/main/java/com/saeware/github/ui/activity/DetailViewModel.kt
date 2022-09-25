package com.saeware.github.ui.activity

import androidx.lifecycle.*
import com.saeware.github.data.GithubRepository
import com.saeware.github.data.Result
import com.saeware.github.data.local.entity.UserEntity
import com.saeware.github.data.remote.response.DetailUser
import kotlinx.coroutines.launch

class DetailViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val _hasLoaded = MutableLiveData(false)
    val hasLoaded: LiveData<Boolean> = _hasLoaded

    private val _user = MutableLiveData<Result<DetailUser>>(Result.Loading)
    val user: LiveData<Result<DetailUser>> = _user

    private val _isFavoriteUser = MutableLiveData(false)
    val isFavoriteUser: LiveData<Boolean> = _isFavoriteUser

    private val hasShownError = MutableLiveData(false)

    fun getUserDetail(username: String) {
        viewModelScope.launch {
            githubRepository.getUserDetail(username).collect { _user.value = it }
        }

        hasShownError.value = false
        _hasLoaded.value = true
    }

    fun isFavoriteUser(username: String) {
        viewModelScope.launch {
            githubRepository.isFavoriteUser(username).collect { _isFavoriteUser.value = it }
        }
    }

    fun addUserAsFavorite(userEntity: UserEntity) {
        viewModelScope.launch { githubRepository.addUserAsFavorite(userEntity) }
    }

    fun removeUserFromFavorite(userEntity: UserEntity) {
        viewModelScope.launch { githubRepository.removeUserFromFavorite(userEntity) }
    }

    fun showErrorOccurred(showError: () -> Unit) {
        if (!hasShownError.value!!) {
            showError()
            hasShownError.value = true
        }
    }
}