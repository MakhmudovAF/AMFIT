package com.example.amfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    Home(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    ),
    Workout(
        route = "workout",
        title = "Workout",
        icon = Icons.Filled.FitnessCenter
    ),
    Profile(
        route = "profile",
        title = "Profile",
        icon = Icons.Filled.Person
    )
}