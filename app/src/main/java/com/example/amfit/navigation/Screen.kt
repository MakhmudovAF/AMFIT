package com.example.amfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.amfit.R

enum class Screen(
    val route: String,
    val titleRes: Int,
    val icon: ImageVector
) {
    Home(
        route = "home",
        titleRes = R.string.nav_home,
        icon = Icons.Filled.Home
    ),
    Workout(
        route = "workout",
        titleRes = R.string.nav_workout,
        icon = Icons.Filled.FitnessCenter
    ),
    Profile(
        route = "profile",
        titleRes = R.string.nav_profile,
        icon = Icons.Filled.Person
    )
}