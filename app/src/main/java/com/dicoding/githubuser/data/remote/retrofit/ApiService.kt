package com.dicoding.githubuser.data.remote.retrofit

import com.dicoding.githubuser.data.remote.response.DetailResponse
import com.dicoding.githubuser.data.remote.response.Item
import com.dicoding.githubuser.data.remote.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String
    ): UserResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<Item>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<Item>
}