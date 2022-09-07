package com.saeware.github.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saeware.github.BuildConfig
import com.saeware.github.model.User
import com.saeware.github.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _followers = MutableLiveData<ArrayList<User>?>(null)
    val followers: LiveData<ArrayList<User>?> = _followers

    fun getUserFollowers(username: String) {
        _loading.value = true

        ApiConfig.getApiService().getUserFollowers(token = BuildConfig.API_KEY, username)
            .apply {
                enqueue(object: Callback<ArrayList<User>> {
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        if (response.isSuccessful) _followers.value = response.body()
                        else Log.e(TAG, "onFailure: ${response.message()}")

                        _loading.value = false
                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message.toString()}")

                        _followers.value = arrayListOf()
                        _loading.value = false
                    }

                })
            }
    }

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
    }
}