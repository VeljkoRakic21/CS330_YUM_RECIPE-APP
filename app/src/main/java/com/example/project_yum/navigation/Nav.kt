package com.example.project_yum.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.project_yum.ui.FavoritesScreen
import com.example.project_yum.ui.LoggedScreen
import com.example.project_yum.ui.LoginScreen
import com.example.project_yum.ui.RecipeScreen
import com.example.project_yum.ui.RegisterScreen
import com.example.project_yum.ui.SearchedScreen
import com.example.project_yum.viewmodel.SearchedViewModel
import com.example.project_yum.viewmodel.RecipeViewModel

@Composable
fun Nav(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginClick = { userId, username ->
                    navController.navigate("logged/$userId/$username")
                },
                onSignUpClick = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable("logged/{userId}/{username}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""

            LoggedScreen(
                userId = userId,
                username = username,
                onSearchClick = { query ->
                    if (query.isNotBlank()) {
                        navController.navigate("searched/$query/$userId/$username")
                    }
                },
                onFavoritesClick = {navController.navigate("favorites/$userId/$username")},
                onFooterSearchClick = {
                    navController.navigate("logged/$userId/$username") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("searched/{query}/{userId}/{username}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val searchedViewModel = remember { SearchedViewModel() }
            SearchedScreen(
                query = query,
                viewModel = searchedViewModel,
                onSearchClick = {
                    navController.popBackStack("logged/$userId/$username", inclusive = false)
                },
                onFavoritesClick = {
                    navController.navigate("favorites/$userId/$username")
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onRecipeClick = { recipe ->
                    navController.navigate("recipe/${recipe.idMeal}/$userId/$username")
                }
            )
        }
        composable("recipe/{idMeal}/{userId}/{username}") { backStackEntry ->
            val idMeal = backStackEntry.arguments?.getString("idMeal") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val recipeDetailViewModel = remember { RecipeViewModel() }
            val uiState by recipeDetailViewModel.uiState.collectAsState()

            LaunchedEffect(idMeal) {
                recipeDetailViewModel.loadRecipe(idMeal)
            }

            RecipeScreen(
                uiState = uiState,
                userId = userId,
                username = username,
                onBackClick = { navController.popBackStack() },
                onFavoritesClick = {navController.navigate("favorites/$userId/$username")},
                onSearchClick = { navController.navigate("logged/$userId/$username") },
            )
        }
        composable("favorites/{userId}/{username}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""
            FavoritesScreen(
                userId = userId,
                onRecipeClick = { recipe ->
                    navController.navigate("recipe/${recipe.idMeal}/$userId/$username")
                },
                onSearchClick = { navController.navigate("logged/$userId/$username") },
                onFavoritesClick = {navController.navigate("favorites/$userId/$username")},
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
