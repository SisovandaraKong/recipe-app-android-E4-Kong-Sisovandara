package com.example.recipefinderapp.data.repository

import com.example.recipefinderapp.data.local.dto.FavouriteDao
import com.example.recipefinderapp.data.local.entity.FavouriteMealEntity
import com.example.recipefinderapp.data.remote.api.ApiService
import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Ingredient
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.util.Resource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favouriteDao: FavouriteDao,
    private val gson: Gson
) : MealRepository {

    override suspend fun getMeals(): Resource<List<Meal>> {
        return try {
            val response = apiService.getMeals()
            val meals = response.map { it.toMeal() }
            Resource.Success(meals)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getMealsByCategory(categoryId: String): Resource<List<Meal>> {
        return try {
            val response = apiService.getMealsByCategory(categoryId)
            val meals = response.map { it.toMeal() }
            Resource.Success(meals)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getMealById(id: String): Resource<Meal> {
        return try {
            val response = apiService.getMealById(id)
            Resource.Success(response.toMeal())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getCategories(): Resource<List<Category>> {
        return try {
            val response = apiService.getCategories()
            val categories = response.map { it.toCategory() }
            Resource.Success(categories)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getRandomMeal(): Resource<Meal> {
        return try {
            val allMeals = apiService.getMeals()
            val randomMeal = allMeals.randomOrNull()?.toMeal()
            if (randomMeal != null) {
                Resource.Success(randomMeal)
            } else {
                Resource.Error("No meals available")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    // Favourite operations
    override fun getFavourites(): Flow<List<Meal>> {
        return favouriteDao.getAllFavourites().map { entities ->
            entities.map { entity ->
                val ingredients: List<Ingredient> = gson.fromJson(
                    entity.ingredientsJson,
                    object : TypeToken<List<Ingredient>>() {}.type
                )
                Meal(
                    id = entity.id,
                    meal = entity.meal,
                    category = entity.category,
                    area = entity.area,
                    instructions = entity.instructions,
                    mealThumb = entity.mealThumb,
                    tags = entity.tags,
                    youtube = entity.youtube,
                    source = entity.source,
                    ingredients = ingredients,
                    categoryId = entity.categoryId,
                    isFavourite = true
                )
            }
        }
    }

    override suspend fun addToFavourites(meal: Meal) {
        val entity = FavouriteMealEntity(
            id = meal.id,
            meal = meal.meal,
            category = meal.category,
            area = meal.area,
            instructions = meal.instructions,
            mealThumb = meal.mealThumb,
            tags = meal.tags,
            youtube = meal.youtube,
            source = meal.source,
            ingredientsJson = gson.toJson(meal.ingredients),
            categoryId = meal.categoryId
        )
        favouriteDao.insertFavourite(entity)
    }

    override suspend fun removeFromFavourites(mealId: String) {
        favouriteDao.deleteFavouriteById(mealId)
    }

    override fun isFavourite(mealId: String): Flow<Boolean> {
        return favouriteDao.isFavourite(mealId)
    }
}