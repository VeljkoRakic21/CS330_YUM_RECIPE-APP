package com.example.project_yum.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.project_yum.model.Recipe
import com.example.project_yum.viewmodel.RecipeDetailUiState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_yum.viewmodel.AddToFavoritesState
import com.example.project_yum.viewmodel.AddToFavoritesViewModel


@Composable
fun RecipeScreen(
    uiState: RecipeDetailUiState,
    userId: String,
    username: String,
    onBackClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSearchClick: () -> Unit,
    addToFavoritesViewModel: AddToFavoritesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val addToFavoritesState by addToFavoritesViewModel.addToFavoritesState.collectAsState()

    LaunchedEffect(addToFavoritesState) {
        when (addToFavoritesState) {
            is AddToFavoritesState.Success -> {
                Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show()
                addToFavoritesViewModel.resetAddToFavoritesState()
            }
            is AddToFavoritesState.Error -> {
                Toast.makeText(
                    context,
                    (addToFavoritesState as AddToFavoritesState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                addToFavoritesViewModel.resetAddToFavoritesState()
            }
            is AddToFavoritesState.AlreadyAdded -> {
                Toast.makeText(context, "Already in favorites.", Toast.LENGTH_SHORT).show()
                addToFavoritesViewModel.resetAddToFavoritesState()
            }
            else -> {}
        }
    }

    when (uiState) {
        is RecipeDetailUiState.Loading, RecipeDetailUiState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is RecipeDetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is RecipeDetailUiState.Success -> {
            val recipe = uiState.recipe
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

                IconButton(
                    onClick = {
                        val recipeId = recipe.idMeal.toIntOrNull()
                        if (recipeId != null) {
                            addToFavoritesViewModel.addRecipeToFavorites(userId, username, recipeId)
                        } else {
                            Toast.makeText(context, "Invalid recipe ID.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Add to Favorites"
                    )
                }

                Text(
                    text = recipe.strMeal,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp, start = 64.dp, end = 64.dp)
                )

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 72.dp, bottom = 96.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                border = ButtonDefaults.outlinedButtonBorder,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(200.dp)
                            ) {
                                AsyncImage(
                                    model = recipe.strMealThumb,
                                    contentDescription = recipe.strMeal,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Category: ${recipe.strCategory ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Text(
                                    text = "Area: ${recipe.strArea ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Ingredients",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                )
                                IngredientsList(recipe = recipe)
                            }
                        }
                    }
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Instructions",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                )
                                Text(
                                    text = recipe.strInstructions ?: "-",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                BottomNavBar(
                    onSearchClick = onSearchClick,
                    onFavoritesClick = onFavoritesClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}


@Composable
fun IngredientsList(recipe: Recipe) {
    val ingredients = remember(recipe) { recipe.ingredientsWithMeasures() }
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ingredients.forEach { (ingredient, measure) ->
            if (ingredient.isNotBlank()) {
                Text(
                    text = "- $ingredient (${measure.trimOrNull()})",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun Recipe.ingredientsWithMeasures(): List<Pair<String, String>> {
    val ingredients = listOf(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
        strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
    )
    val measures = listOf(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
        strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
    )
    return ingredients.zip(measures)
        .filter { (ingredient, _) -> !ingredient.isNullOrBlank() }
        .map { (ingredient, measure) -> ingredient.orEmpty() to (measure ?: "") }
}

fun String?.trimOrNull(): String = this?.trim().takeUnless { it.isNullOrEmpty() } ?: "-"