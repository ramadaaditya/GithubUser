package com.dicoding.core.utils

import com.dicoding.core.data.source.local.entity.UserEntity
import com.dicoding.core.data.source.remote.response.UserGithub
import com.dicoding.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapResponsesToDomain(input: List<UserGithub>): Flow<List<User>> {
        val userList = ArrayList<User>()
        input.map {
            val user = User(
                id = it.id,
                name = it.name,
                avatarUrl = it.avatarUrl,
                login = it.login,
                followers = it.followers,
                following = it.following,
                isFavorite = false
            )
            userList.add(user)
        }
        return flowOf(userList)
    }

    fun mapResponseToDomain(input: UserGithub): Flow<User> {
        return flowOf(
            User(
                input.id,
                input.login,
                input.avatarUrl,
                input.name,
                input.followers,
                input.following,
                false
            )
        )
    }

    fun mapEntityToDomain(input: UserEntity?): User {
        return User(
            input?.id,
            input?.login,
            input?.avatarUrl,
            input?.name,
            input?.followers,
            input?.following,
            input?.isFavorite
        )
    }

    fun mapEntitiesToDomain(input: List<UserEntity>): List<User> =
        input.map { userEntity ->
            User(
                userEntity.id,
                userEntity.login,
                userEntity.avatarUrl,
                userEntity.name,
                userEntity.followers,
                userEntity.following,
                userEntity.isFavorite
            )
        }

    fun mapDomainToEntity(input: User) = UserEntity(
        input.id,
        input.login,
        input.avatarUrl,
        input.name,
        input.followers,
        input.following,
        input.isFavorite
    )
}