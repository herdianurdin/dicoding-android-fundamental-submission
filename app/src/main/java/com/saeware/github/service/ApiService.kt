package com.saeware.github.service

import com.saeware.github.model.DetailUser
import com.saeware.github.model.ResponseSearch
import com.saeware.github.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUserByUsername(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Call<ResponseSearch>

    @GET("users/{username}")
    fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<DetailUser>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<ArrayList<User>>
}