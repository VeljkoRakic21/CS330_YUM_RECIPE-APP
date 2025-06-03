package com.example.project_yum.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_yum.retro.UserRetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class AddToFavoritesState {
    object Idle : AddToFavoritesState()
    object Loading : AddToFavoritesState()
    object Success : AddToFavoritesState()
    data class Error(val message: String) : AddToFavoritesState()
    object AlreadyAdded : AddToFavoritesState()
}

@HiltViewModel
class AddToFavoritesViewModel @Inject constructor() : ViewModel() {

    private val _addToFavoritesState = MutableStateFlow<AddToFavoritesState>(AddToFavoritesState.Idle)
    val addToFavoritesState: StateFlow<AddToFavoritesState> = _addToFavoritesState

    fun addRecipeToFavorites(userId: String, username: String, recipeId: Int) {
        _addToFavoritesState.value = AddToFavoritesState.Loading
        viewModelScope.launch {
            try {
                val users = UserRetrofitInstance.api.getUserByUsername(username)
                val user = users.firstOrNull()

                if (user != null) {
                    val favorites = user.favorites.toMutableList()

                    if (favorites.contains(recipeId)) {
                        _addToFavoritesState.value = AddToFavoritesState.AlreadyAdded
                    } else {
                        favorites.add(recipeId)
                        val patch = mapOf("favorites" to favorites)
                        val response = UserRetrofitInstance.api.patchUserFavorites(userId, patch)

                        if (!response.isSuccessful) {
                            val errorBody = response.errorBody()?.string()
                        }
                        if (response.isSuccessful) {
                            _addToFavoritesState.value = AddToFavoritesState.Success
                        } else {
                            _addToFavoritesState.value = AddToFavoritesState.Error("Failed to add to favorites.")
                        }
                    }
                } else {
                    _addToFavoritesState.value = AddToFavoritesState.Error("User not found.")
                }
            } catch (e: Exception) {
                _addToFavoritesState.value = AddToFavoritesState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun resetAddToFavoritesState() {
        _addToFavoritesState.value = AddToFavoritesState.Idle
    }
}
