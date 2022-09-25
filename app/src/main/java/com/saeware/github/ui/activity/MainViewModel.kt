package com.saeware.github.ui.activity

import androidx.lifecycle.*
import com.saeware.github.data.GithubRepository
import com.saeware.github.data.Result
import com.saeware.github.data.remote.response.User
import kotlinx.coroutines.launch

class MainViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val _users = MutableLiveData<Result<ArrayList<User>>>(Result.Loading)
    val users: LiveData<Result<ArrayList<User>>> = _users

    private val hasShownError = MutableLiveData(false)

    init { searchUserByUsername() }

    fun getDarkModeSetting(): LiveData<Boolean> =
        githubRepository.getDarkModeSetting().asLiveData()

    fun searchUserByUsername(query: String = "\"\"") {
        _users.value = Result.Loading
        viewModelScope.launch {
            githubRepository.searchUserByUsername(query).collect { _users.value = it }
        }
        hasShownError.value = false
    }

    fun showErrorOccurred(showError: () -> Unit) {
        if (!hasShownError.value!!) {
            showError()
            hasShownError.value = true
        }
    }
}