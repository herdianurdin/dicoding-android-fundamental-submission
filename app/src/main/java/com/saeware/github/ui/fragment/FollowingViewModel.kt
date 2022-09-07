package com.saeware.github.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saeware.github.BuildConfig
import com.saeware.github.R
import com.saeware.github.model.User
import com.saeware.github.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _following = MutableLiveData<ArrayList<User>?>(null)
    val following: LiveData<ArrayList<User>?> = _following

    fun getUserFollowing(username: String) {
        _loading.value = true

        ApiConfig.getApiService().getUserFollowing(token = BuildConfig.API_KEY, username)
            .apply {
                enqueue(object: Callback<ArrayList<User>> {
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        if (response.isSuccessful) _following.value = response.body()
                        else Log.e(TAG, "onFailure: ${response.message()}")

                        _loading.value = false
                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message.toString()}")

                        _following.value = arrayListOf()
                        _loading.value = false
                    }

                })
            }
    }

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }
}