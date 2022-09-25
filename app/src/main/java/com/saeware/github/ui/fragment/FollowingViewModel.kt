package com.saeware.github.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeware.github.data.GithubRepository
import com.saeware.github.data.remote.response.User
import com.saeware.github.data.Result
import kotlinx.coroutines.launch

class FollowingViewModel(private val githubRepository: GithubRepository) : ViewModel() {
    private val _hasLoaded = MutableLiveData(false)
    val hasLoaded = _hasLoaded

    private val _following = MutableLiveData<Result<ArrayList<User>>>(Result.Loading)
    val following: LiveData<Result<ArrayList<User>>> = _following

    fun getUserFollowing(username: String) {
        _following.value = Result.Loading
        viewModelScope.launch {
            githubRepository.getUserFollowing(username).collect { _following.value = it }
        }
        _hasLoaded.value = true
    }
}