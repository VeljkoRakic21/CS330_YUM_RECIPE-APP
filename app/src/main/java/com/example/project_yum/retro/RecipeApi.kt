package com.example.project_yum.retro

import com.example.project_yum.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("api/json/v1/1/search.php")
    suspend fun searchRecipes(@Query("s") query: String): RecipeResponse

    @GET("api/json/v1/1/lookup.php")
    suspend fun getRecipeById(@Query("i") id: String): RecipeResponse
}