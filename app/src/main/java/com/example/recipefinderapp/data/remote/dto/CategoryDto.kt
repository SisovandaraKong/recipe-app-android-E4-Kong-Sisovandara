package com.example.recipefinderapp.data.remote.dto

import com.example.recipefinderapp.domain.model.Category

data class CategoryDto(
    val id: String,
    val category: String,
    val categoryThumb: String,
    val categoryDescription: String
) {
    fun toCategory() = Category(
        id = id,
        category = category,
        categoryThumb = categoryThumb,
        categoryDescription = categoryDescription
    )
}