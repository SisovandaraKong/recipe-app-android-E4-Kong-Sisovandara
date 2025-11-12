package com.example.recipefinderapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipefinderapp.data.local.dto.FavouriteDao
import com.example.recipefinderapp.data.local.entity.FavouriteMealEntity

@Database(
    entities = [FavouriteMealEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
}