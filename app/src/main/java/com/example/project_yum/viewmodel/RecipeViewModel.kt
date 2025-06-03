package com.example.project_yum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_yum.model.Recipe
import com.example.project_yum.retro.RecipeRetrofitInstance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class RecipeDetailUiState {
    object Idle : RecipeDetailUiState()
    object Loading : RecipeDetailUiState()
    data class Success(val recipe: Recipe) : RecipeDetailUiState()
    data class Error(val message: String) : RecipeDetailUiState()
}

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeDetailUiState>(RecipeDetailUiState.Idle)
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(id: String) {
        _uiState.value = RecipeDetailUiState.Loading
        viewModelScope.launch {
            try {
                val response = RecipeRetrofitInstance.api.getRecipeById(id)
                val recipe = response.meals?.firstOrNull()
                if (recipe != null) {
                    _uiState.value = RecipeDetailUiState.Success(recipe)
                } else {
                    _uiState.value = RecipeDetailUiState.Error("Recipe not found")
                }
            } catch (e: Exception) {
                _uiState.value = RecipeDetailUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}