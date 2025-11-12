package com.example.recipefinderapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinderapp.data.repository.MealRepository
import com.example.recipefinderapp.domain.model.Category
import com.example.recipefinderapp.domain.model.Meal
import com.example.recipefinderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Load popular meals
            when (val mealsResult = repository.getMeals()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            popularMeals = mealsResult.data?.take(10) ?: emptyList()
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = mealsResult.message) }
                }
                else -> {}
            }

            // Load random meal
            when (val randomResult = repository.getRandomMeal()) {
                is Resource.Success -> {
                    _uiState.update { it.copy(randomMeal = randomResult.data) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(error = randomResult.message) }
                }
                else -> {}
            }

            // Load categories
            when (val categoriesResult = repository.getCategories()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            categories = categoriesResult.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = categoriesResult.message,
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun retry() {
        loadHomeData()
    }
}

data class HomeUiState(
    val popularMeals: List<Meal> = emptyList(),
    val randomMeal: Meal? = null,
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)