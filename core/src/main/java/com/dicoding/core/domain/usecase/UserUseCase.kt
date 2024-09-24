package com.dicoding.core.domain.usecase

import com.dicoding.core.data.source.Resource
import com.dicoding.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun getSearchUsers(query: String?): Flow<Resource<List<User>>>
    fun getDetailUser(username: String): Flow<Resource<User>>
    fun getFollowers(username: String): Flow<Resource<List<User>>>
    fun getFollowing(username: String): Flow<Resource<List<User>>>
    fun getDataByUsername(username: String): Flow<User>?
    fun getAllFavorite(): Flow<List<User>>
    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User)
}