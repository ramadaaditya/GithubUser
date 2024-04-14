package com.dicoding.githubuser.repository

import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.local.room.UserDao
import com.dicoding.githubuser.data.remote.response.DetailResponse
import com.dicoding.githubuser.data.remote.response.Item
import com.dicoding.githubuser.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository private constructor(
    private val apiService: ApiService,
    private val mUserDao: UserDao,
) {
    suspend fun searchUsers(query: String): List<Item> {
        return withContext(Dispatchers.IO) {
            val response = apiService.searchUser(query)
            response.items
        }
    }

    suspend fun getDetailUser(username: String): DetailResponse {
        return withContext(Dispatchers.IO) {
            apiService.getDetailUser(username)
        }
    }

    suspend fun getFollowers(username: String): List<Item> {
        return withContext(Dispatchers.IO) {
            apiService.getFollowers(username)
        }
    }

    suspend fun getFollowing(username: String): List<Item> {
        return withContext(Dispatchers.IO) {
            apiService.getFollowing(username)
        }
    }

    fun getAllFavorite(): Flow<List<UserEntity>> = mUserDao.getAllFavorite()
    fun getDataByUsername(username: String): LiveData<List<UserEntity>> =
        mUserDao.getDataByUsername(username)

    suspend fun insertUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            mUserDao.insert(user)
        }
    }

    suspend fun deleteUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            mUserDao.delete(user)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, userDao: UserDao): UserRepository {
            return instance ?: synchronized(this) {
                UserRepository(apiService, userDao).also {
                    instance = it
                }
            }
        }
    }
}