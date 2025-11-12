package com.example.recipefinderapp.data.remote.api

import com.example.recipefinderapp.data.remote.dto.CategoryDto
import com.example.recipefinderapp.data.remote.dto.MealDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("meals")
    suspend fun getMeals(): List<MealDto>

    @GET("meals")
    suspend fun getMealsByCategory(
        @Query("categoryId") categoryId: String
    ): List<MealDto>

    @GET("meals/{id}")
    suspend fun getMealById(@Path("id") id: String): MealDto

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}