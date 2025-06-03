package com.example.project_yum.retro

import com.example.project_yum.model.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserApi {
    @POST("users")
    fun registerUser(@Body user: User): Call<User>

    @GET("users")
    suspend fun getUserByUsername(@Query("username") username: String): List<User>

    @GET("users")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): List<User>

    @PATCH("users/{id}")
    @JvmSuppressWildcards
    suspend fun patchUserFavorites(
        @Path("id") userId: String,
        @Body patch: Map<String, Any>
    ): Response<Unit>

    @GET("users")
    suspend fun getUserById(@Query("id") id: String): List<User>
}