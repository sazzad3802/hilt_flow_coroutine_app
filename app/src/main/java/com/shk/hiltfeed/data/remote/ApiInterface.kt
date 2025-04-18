package com.shk.hiltfeed.data.remote

import User
import UserData
import com.shk.hiltfeed.data.models.PostData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
    @GET("/posts")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("skip") page: Int
    ): Response<PostData>

    @GET("/users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("skip") page: Int
    ): Response<UserData>

    @GET("/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Long,
    ): Response<User>
}
