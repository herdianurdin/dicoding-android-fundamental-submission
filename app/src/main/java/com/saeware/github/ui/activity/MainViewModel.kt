package com.saeware.github.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saeware.github.BuildConfig
import com.saeware.github.model.ResponseSearch
import com.saeware.github.model.User
import com.saeware.github.service.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean> = _error

    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>> = _users

    init { searchUserByUsername() }

    fun searchUserByUsername(query: String = "\"\"") {
        _loading.value = true

        ApiConfig.getApiService().searchUserByUsername(token = BuildConfig.API_KEY, query).apply {
            enqueue(object : Callback<ResponseSearch> {
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    if (response.isSuccessful) _users.value = response.body()?.items
                    else Log.e(TAG, "onFailure: ${response.message()}")

                    _loading.value = false
                    _error.value = false
                }

                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")

                    _users.value = arrayListOf()
                    _loading.value = false
                    _error.value = true
                }

            })
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}