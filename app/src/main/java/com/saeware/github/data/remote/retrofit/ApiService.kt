package com.saeware.github.data.remote.retrofit

import com.saeware.github.data.remote.response.DetailUser
import com.saeware.github.data.remote.response.ResponseSearch
import com.saeware.github.data.remote.response.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun searchUserByUsername(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): ResponseSearch

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): DetailUser

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): ArrayList<User>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): ArrayList<User>
}