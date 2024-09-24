package com.dicoding.core.data.source.remote.network

import com.dicoding.core.data.source.remote.response.UserGithub
import com.dicoding.core.data.source.remote.response.GithubResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): UserGithub

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<UserGithub>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<UserGithub>
}