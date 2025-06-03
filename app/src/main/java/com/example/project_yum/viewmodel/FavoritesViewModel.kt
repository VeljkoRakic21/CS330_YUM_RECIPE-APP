package com.example.project_yum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_yum.model.Recipe
import com.example.project_yum.retro.RecipeRetrofitInstance
import com.example.project_yum.retro.UserRetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val recipes: List<Recipe>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
    object Empty : FavoritesUiState()
}

class FavoritesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState

    fun loadFavorites(userId: String) {
        _uiState.value = FavoritesUiState.Loading
        viewModelScope.launch {
            try {
                val users = UserRetrofitInstance.api.getUserById(userId)
                val user = users.firstOrNull()

                if (user == null) {
                    _uiState.value = FavoritesUiState.Error("User not found.")
                    return@launch
                }
                val favorites = user.favorites
                if (favorites.isEmpty()) {
                    _uiState.value = FavoritesUiState.Empty
                    return@launch
                }

                val recipes = mutableListOf<Recipe>()
                for (id in favorites) {
                    val response = RecipeRetrofitInstance.api.getRecipeById(id.toString())
                    val recipe = response.meals?.firstOrNull()
                    if (recipe != null) recipes.add(recipe)
                }
                if (recipes.isEmpty()) {
                    _uiState.value = FavoritesUiState.Empty
                } else {
                    _uiState.value = FavoritesUiState.Success(recipes)
                }
            } catch (e: Exception) {
                _uiState.value = FavoritesUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun removeRecipeFromFavorites(userId: String, recipeId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val users = UserRetrofitInstance.api.getUserById(userId)
                val user = users.firstOrNull()
                if (user != null) {
                    val updatedFavorites = user.favorites.filter { it != recipeId }
                    val patch = mapOf("favorites" to updatedFavorites)
                    val response = UserRetrofitInstance.api.patchUserFavorites(userId, patch)
                    if (response.isSuccessful) {
                        loadFavorites(userId)
                        onSuccess()
                    }
                }
            } catch (_: Exception) { }
        }
    }
}
