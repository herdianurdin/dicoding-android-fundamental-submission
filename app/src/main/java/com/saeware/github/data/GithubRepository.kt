package com.saeware.github.data

import android.util.Log
import com.saeware.github.BuildConfig.API_KEY
import com.saeware.github.data.local.entity.UserEntity
import com.saeware.github.data.local.room.UserDao
import com.saeware.github.data.remote.response.DetailUser
import com.saeware.github.data.remote.response.User
import com.saeware.github.data.remote.retrofit.ApiService
import com.saeware.github.utils.EspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GithubRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val appPreferences: AppPreferences
) {
    fun searchUserByUsername(query: String): Flow<Result<ArrayList<User>>> = flow {
        EspressoIdlingResource.increment()
        emit(Result.Loading)
        try {
            val users = apiService.searchUserByUsername(token = API_KEY, query).items
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d(TAG, "searchUserByUsername: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        } finally { EspressoIdlingResource.decrement() }
    }

    fun getUserDetail(username: String): Flow<Result<DetailUser>> = flow {
        EspressoIdlingResource.increment()
        emit(Result.Loading)
        try {
            val user = apiService.getUserDetail(token = API_KEY, username)
            emit(Result.Success(user))
        } catch (e: Exception) {
            Log.d(TAG, "getUserDetail: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        } finally { EspressoIdlingResource.decrement() }
    }

    fun getUserFollowers(username: String): Flow<Result<ArrayList<User>>> = flow {
        EspressoIdlingResource.increment()
        emit(Result.Loading)
        try {
            val followers = apiService.getUserFollowers(token = API_KEY, username)
            emit(Result.Success(followers))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowers: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        } finally { EspressoIdlingResource.decrement() }
    }

    fun getUserFollowing(username: String): Flow<Result<ArrayList<User>>> = flow {
        EspressoIdlingResource.increment()
        emit(Result.Loading)
        try {
            val following = apiService.getUserFollowing(token = API_KEY, username)
            emit(Result.Success(following))
        } catch (e: Exception) {
            Log.d(TAG, "getUserFollowing: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        } finally { EspressoIdlingResource.decrement() }
    }

    fun getDarkModeSetting(): Flow<Boolean> = appPreferences.getDarkModeSetting()

    suspend fun saveDarkModeSetting(state: Boolean) { appPreferences.saveDarkModeSetting(state) }

    fun getFavoriteUsers(): Flow<List<UserEntity>> = userDao.getUsers()

    fun isFavoriteUser(username: String): Flow<Boolean> = userDao.isFavoriteUser(username)

    suspend fun addUserAsFavorite(userEntity: UserEntity) { userDao.insert(userEntity) }

    suspend fun removeUserFromFavorite(userEntity: UserEntity) { userDao.delete(userEntity) }

    companion object {
        private val TAG = GithubRepository::class.java.simpleName

        @Volatile
        private var instance: GithubRepository? = null
        fun getRepository(
            apiService: ApiService,
            userDao: UserDao,
            appPreferences: AppPreferences
        ): GithubRepository =
            instance ?: synchronized(this) {
                instance ?: GithubRepository(apiService, userDao, appPreferences)
            }.also { instance = it }
    }
}