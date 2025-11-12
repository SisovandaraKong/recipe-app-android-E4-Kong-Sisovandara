package com.example.recipefinderapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipefinderapp.presentation.detail.MealDetailScreen
import com.example.recipefinderapp.presentation.explore.ExploreScreen
import com.example.recipefinderapp.presentation.favourite.FavouriteScreen
import com.example.recipefinderapp.presentation.home.HomeScreen
import com.example.recipefinderapp.presentation.onboarding.OnboardingScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Onboarding.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onMealClick = { mealId ->
                    navController.navigate(Screen.MealDetail.createRoute(mealId))
                }
            )
        }

        composable(Screen.Explore.route) {
            ExploreScreen(
                onMealClick = { mealId ->
                    navController.navigate(Screen.MealDetail.createRoute(mealId))
                }
            )
        }

        composable(Screen.Favourite.route) {
            FavouriteScreen(
                onMealClick = { mealId ->
                    navController.navigate(Screen.MealDetail.createRoute(mealId))
                }
            )
        }

        composable(
            route = Screen.MealDetail.route,
            arguments = listOf(
                navArgument("mealId") { type = NavType.StringType }
            )
        ) {
            MealDetailScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}