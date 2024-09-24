package com.dicoding.core.data.source.remote

import android.content.ContentValues.TAG
import android.util.Log
import com.dicoding.core.data.source.remote.network.ApiResponse
import com.dicoding.core.data.source.remote.network.ApiService
import com.dicoding.core.data.source.remote.response.UserGithub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun searchUsers(query: String?): Flow<ApiResponse<List<UserGithub>>> = flow {
        try {
            val response = apiService.searchUser(query!!)
            if (response.items.isNotEmpty()) {
                emit(ApiResponse.Success(response.items))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailUser(username: String): Flow<ApiResponse<UserGithub>> = flow {
        try {
            val response = apiService.getDetailUser(username)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getFollowers(username: String): Flow<ApiResponse<List<UserGithub>>> = flow {
        try {
            val response = apiService.getFollowers(username)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e(TAG, "getFollowing: ${e.localizedMessage}")
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getFollowing(username: String): Flow<ApiResponse<List<UserGithub>>> = flow {
        try {
            val response = apiService.getFollowing(username)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
            Log.e(TAG, "getFollowing: ${e.localizedMessage}")
        }
    }.flowOn(Dispatchers.IO)
}