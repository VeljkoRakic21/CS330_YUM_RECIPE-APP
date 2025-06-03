package com.example.project_yum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_yum.model.Recipe
import com.example.project_yum.viewmodel.SearchUiState
import com.example.project_yum.viewmodel.SearchedViewModel
import com.example.project_yum.ui.RecipeCard

@Composable
fun SearchedScreen(
    query: String,
    viewModel: SearchedViewModel,
    onRecipeClick: (Recipe) -> Unit,
    onSearchClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(query) {
        viewModel.searchRecipes(query)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Text(
            text = "Results for: \"$query\"",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp, bottom = 96.dp)
        ) {
            when (uiState) {
                is SearchUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is SearchUiState.Success -> {
                    val recipes = (uiState as SearchUiState.Success).recipes
                    if (recipes.isEmpty()) {
                        Text(
                            text = "No results for \"$query\".",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(recipes) { recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    onClick = { onRecipeClick(recipe) }
                                    )
                            }
                        }
                    }
                }
                is SearchUiState.Error -> {
                    Text(
                        text = "Error: ${(uiState as SearchUiState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                SearchUiState.Idle -> {}
            }
        }

        BottomNavBar(
            onSearchClick = onSearchClick,
            onFavoritesClick = onFavoritesClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}
