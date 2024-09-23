package com.dicoding.core.domain.usecase

import com.dicoding.core.data.source.Resource
import com.dicoding.core.domain.model.User
import com.dicoding.core.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow

class UserInteract(private val repository: IUserRepository) : UserUseCase {
    override fun getSearchUsers(query: String?): Flow<Resource<List<User>>> =
        repository.getSearchUsers(query)

    override fun getDetailUser(username: String): Flow<Resource<User>> =
        repository.getDetailUser(username)

    override fun getFollowers(username: String): Flow<Resource<List<User>>> =
        repository.getFollowers(username)

    override fun getFollowing(username: String): Flow<Resource<List<User>>> =
        repository.getFollowing(username)

    override fun getDataByUsername(username: String): Flow<User>? =
        repository.getDataByUsername(username)

    override fun getAllFavorite(): Flow<List<User>> = repository.getAllFavorite()

    override suspend fun insertUser(user: User) = repository.insertUser(user)

    override suspend fun deleteUser(user: User) = repository.deleteUser(user)
}