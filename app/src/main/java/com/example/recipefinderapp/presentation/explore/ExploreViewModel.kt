package com.example.recipefinderapp.presentation.explore

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
class ExploreViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.getCategories()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            categories = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    // Load all meals initially
                    loadMeals(null)
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
        loadMeals(categoryId)
    }

    private fun loadMeals(categoryId: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMeals = true) }

            val result = if (categoryId == null) {
                repository.getMeals()
            } else {
                repository.getMealsByCategory(categoryId)
            }

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            meals = result.data ?: emptyList(),
                            isLoadingMeals = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoadingMeals = false
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

data class ExploreUiState(
    val categories: List<Category> = emptyList(),
    val meals: List<Meal> = emptyList(),
    val selectedCategoryId: String? = null,
    val isLoading: Boolean = false,
    val isLoadingMeals: Boolean = false,
    val error: String? = null
)