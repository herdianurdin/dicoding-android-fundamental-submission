package com.saeware.github.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saeware.github.BuildConfig
import com.saeware.github.model.DetailUser
import com.saeware.github.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    private val _hasLoaded = MutableLiveData(false)
    val hasLoaded: LiveData<Boolean> = _hasLoaded

    private val _user = MutableLiveData<DetailUser?>()
    val user: LiveData<DetailUser?> = _user

    fun getUserDetail(username: String) {
        _loading.value = true
        _hasLoaded.value = true

        ApiConfig.getApiService().getUserDetail(token = BuildConfig.API_KEY, username).apply {
            enqueue(object : Callback<DetailUser> {
                override fun onResponse(call: Call<DetailUser>, response: Response<DetailUser>) {
                    if (response.isSuccessful) _user.value = response.body()
                    else Log.e(TAG, "onFailure: ${response.message()}")

                    _loading.value = false
                    _error.value = false
                }

                override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")

                    _loading.value = false
                    _error.value = true
                }
            })
        }
    }

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }
}