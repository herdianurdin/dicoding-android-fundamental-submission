package com.saeware.github.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeware.github.data.GithubRepository
import com.saeware.github.data.remote.response.User
import com.saeware.github.data.Result
import kotlinx.coroutines.launch

class FollowersViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val _hasLoaded = MutableLiveData(false)
    val hasLoaded: LiveData<Boolean> = _hasLoaded

    private val _followers = MutableLiveData<Result<ArrayList<User>>>(Result.Loading)
    val followers: LiveData<Result<ArrayList<User>>> = _followers

    fun getUserFollowers(username: String) {
        _followers.value = Result.Loading
        viewModelScope.launch {
            githubRepository.getUserFollowers(username).collect { _followers.value = it }
        }
        _hasLoaded.value = true
    }
}