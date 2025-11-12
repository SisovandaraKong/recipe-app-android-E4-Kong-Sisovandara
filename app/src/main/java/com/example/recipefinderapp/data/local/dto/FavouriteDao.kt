package com.example.recipefinderapp.data.local.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipefinderapp.data.local.entity.FavouriteMealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourite_meals ORDER BY timestamp DESC")
    fun getAllFavourites(): Flow<List<FavouriteMealEntity>>

    @Query("SELECT * FROM favourite_meals WHERE id = :mealId")
    suspend fun getFavouriteById(mealId: String): FavouriteMealEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(meal: FavouriteMealEntity)

    @Delete
    suspend fun deleteFavourite(meal: FavouriteMealEntity)

    @Query("DELETE FROM favourite_meals WHERE id = :mealId")
    suspend fun deleteFavouriteById(mealId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_meals WHERE id = :mealId)")
    fun isFavourite(mealId: String): Flow<Boolean>
}