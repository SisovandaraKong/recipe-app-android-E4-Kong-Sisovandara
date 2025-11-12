package com.example.recipefinderapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_meals")
data class FavouriteMealEntity(
    @PrimaryKey val id: String,
    val meal: String,
    val category: String,
    val area: String,
    val instructions: String,
    val mealThumb: String,
    val tags: String?,
    val youtube: String?,
    val source: String?,
    val ingredientsJson: String, // Store as JSON string
    val categoryId: String,
    val timestamp: Long = System.currentTimeMillis()
)