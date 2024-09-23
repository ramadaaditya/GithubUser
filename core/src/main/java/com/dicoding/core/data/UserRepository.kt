package com.dicoding.core.data

import com.dicoding.core.data.source.Resource
import com.dicoding.core.data.source.local.LocalDataSource
import com.dicoding.core.data.source.remote.NetworkOnlyResource
import com.dicoding.core.data.source.remote.RemoteDataSource
import com.dicoding.core.data.source.remote.network.ApiResponse
import com.dicoding.core.data.source.remote.response.UserGithub
import com.dicoding.core.domain.model.User
import com.dicoding.core.domain.repository.IUserRepository
import com.dicoding.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : IUserRepository {
    override fun getSearchUsers(query: String?): Flow<Resource<List<User>>> {
        return object : NetworkOnlyResource<List<User>, List<UserGithub>>() {
            override fun loadFromNetwork(data: List<UserGithub>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserGithub>>> {
                return remoteDataSource.searchUsers(query)
            }

        }.asFlow()
    }

    override fun getDetailUser(username: String): Flow<Resource<User>> {
        return object : NetworkOnlyResource<User, UserGithub>() {
            override fun loadFromNetwork(data: UserGithub): Flow<User> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<UserGithub>> {
                return remoteDataSource.getDetailUser(username)
            }

        }.asFlow()
    }

    override fun getFollowers(username: String): Flow<Resource<List<User>>> {
        return object : NetworkOnlyResource<List<User>, List<UserGithub>>() {
            override fun loadFromNetwork(data: List<UserGithub>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserGithub>>> {
                return remoteDataSource.getFollowers(username)
            }

        }.asFlow()
    }

    override fun getFollowing(username: String): Flow<Resource<List<User>>> {
        return object : NetworkOnlyResource<List<User>, List<UserGithub>>() {
            override fun loadFromNetwork(data: List<UserGithub>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserGithub>>> {
                return remoteDataSource.getFollowing(username)
            }
        }.asFlow()
    }

    override fun getDataByUsername(username: String): Flow<User> {
        return localDataSource.getDataByUsername(username).map {
            DataMapper.mapEntityToDomain(it)
        }
    }

    override fun getAllFavorite(): Flow<List<User>> {
        return localDataSource.getAllFavorite().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun insertUser(user: User) {
        val domainUser = DataMapper.mapDomainToEntity(user)
        return localDataSource.insertUser(domainUser)
    }

    override suspend fun deleteUser(user: User) {
        val domainUser = DataMapper.mapDomainToEntity(user)
        return localDataSource.deleteUser(domainUser)
    }
}