package com.example.recipefinderapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinderapp.data.repository.MealRepository
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
class MealDetailViewModel @Inject constructor(
    private val repository: MealRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mealId: String = checkNotNull(savedStateHandle["mealId"])

    private val _uiState = MutableStateFlow(MealDetailUiState())
    val uiState: StateFlow<MealDetailUiState> = _uiState.asStateFlow()

    init {
        loadMealDetail()
        observeFavouriteStatus()
    }

    private fun loadMealDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.getMealById(mealId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            meal = result.data,
                            isLoading = false
                        )
                    }
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

    private fun observeFavouriteStatus() {
        viewModelScope.launch {
            repository.isFavourite(mealId).collect { isFav ->
                _uiState.update { it.copy(isFavourite = isFav) }
            }
        }
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            val meal = _uiState.value.meal ?: return@launch

            if (_uiState.value.isFavourite) {
                repository.removeFromFavourites(meal.id)
            } else {
                repository.addToFavourites(meal)
            }
        }
    }
}

data class MealDetailUiState(
    val meal: Meal? = null,
    val isFavourite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)