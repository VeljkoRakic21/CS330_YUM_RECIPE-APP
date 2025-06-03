package com.example.project_yum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_yum.model.Recipe
import com.example.project_yum.retro.RecipeRetrofitInstance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val recipes: List<Recipe>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

class SearchedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun searchRecipes(query: String) {
        _uiState.value = SearchUiState.Loading
        viewModelScope.launch {
            try {
                val response = RecipeRetrofitInstance.api.searchRecipes(query)
                val recipes = response.meals ?: emptyList()
                _uiState.value = SearchUiState.Success(recipes)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}