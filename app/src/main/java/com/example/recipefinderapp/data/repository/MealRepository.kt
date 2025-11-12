package com.example.recipefinderapp.data.repository

import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    suspend fun getMeals(): Resource<List<Meal>>
    suspend fun getMealsByCategory(categoryId: String): Resource<List<Meal>>
    suspend fun getMealById(id: String): Resource<Meal>
    suspend fun getCategories(): Resource<List<Category>>
    suspend fun getRandomMeal(): Resource<Meal>

    // Favourites
    fun getFavourites(): Flow<List<Meal>>
    suspend fun addToFavourites(meal: Meal)
    suspend fun removeFromFavourites(mealId: String)
    fun isFavourite(mealId: String): Flow<Boolean>
}