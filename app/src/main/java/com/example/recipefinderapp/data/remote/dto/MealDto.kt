package com.example.recipefinderapp.data.remote.dto

import com.example.recipefinderapp.domain.model.Ingredient
import com.example.recipefinderapp.domain.model.Meal

data class MealDto(
    val id: String,
    val meal: String,
    val drinkAlternate: String?,
    val category: String,
    val area: String,
    val instructions: String,
    val mealThumb: String,
    val tags: String?,
    val youtube: String?,
    val source: String?,
    val ingredients: List<IngredientDto>,
    val categoryId: String
) {
    fun toMeal() = Meal(
        id = id,
        meal = meal,
        category = category,
        area = area,
        instructions = instructions,
        mealThumb = mealThumb,
        tags = tags,
        youtube = youtube,
        source = source,
        ingredients = ingredients.map {
            Ingredient(it.ingredient, it.measure)
        },
        categoryId = categoryId
    )
}