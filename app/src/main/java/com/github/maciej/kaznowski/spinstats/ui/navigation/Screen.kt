package com.github.maciej.kaznowski.spinstats.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Activities : Screen("activities")
    object ActivityDetail : Screen("activity/{activityId}") {
        fun createRoute(activityId: String): String = "activity/$activityId"
    }
    object Settings : Screen("settings")
}
