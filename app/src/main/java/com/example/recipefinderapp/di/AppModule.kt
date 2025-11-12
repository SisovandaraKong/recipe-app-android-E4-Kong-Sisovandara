package com.example.recipefinderapp.di

import com.example.recipefinderapp.data.local.dto.FavouriteDao
import com.example.recipefinderapp.data.remote.api.ApiService
import com.example.recipefinderapp.data.repository.MealRepository
import com.example.recipefinderapp.data.repository.MealRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMealRepository(
        apiService: ApiService,
        favouriteDao: FavouriteDao,
        gson: Gson
    ): MealRepository {
        return MealRepositoryImpl(apiService, favouriteDao, gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}