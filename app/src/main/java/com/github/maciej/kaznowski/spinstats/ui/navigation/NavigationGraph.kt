package com.github.maciej.kaznowski.spinstats.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.maciej.kaznowski.spinstats.ui.screens.activities.ActivitiesScreen
import com.github.maciej.kaznowski.spinstats.ui.screens.activitydetail.ActivityDetailScreen
import com.github.maciej.kaznowski.spinstats.ui.screens.home.HomeScreen
import com.github.maciej.kaznowski.spinstats.ui.screens.settings.SettingsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToActivities = { navController.navigate(Screen.Activities.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Activities.route) {
            ActivitiesScreen(
                onNavigateToDetail = { activityId ->
                    navController.navigate(Screen.ActivityDetail.createRoute(activityId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ActivityDetail.route) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
            ActivityDetailScreen(
                activityId = activityId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
