package com.example.amfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.amfit.R
import com.example.amfit.data.TopBarAction

enum class Screen(
    val route: String,
    val titleRes: Int,
    val icon: ImageVector,
    val actions: List<TopBarAction> = emptyList()
) {
    Home(
        route = "home",
        titleRes = R.string.nav_home,
        icon = Icons.Filled.Home,
        actions = listOf(
            TopBarAction(
                icon = Icons.Outlined.Notifications,
                contentDescriptionRes = R.string.action_notifications
            ),
            TopBarAction(
                icon = Icons.Outlined.MoreVert,
                contentDescriptionRes = R.string.action_more
            )
        )
    ),
    Workout(
        route = "workout",
        titleRes = R.string.nav_workout,
        icon = Icons.Filled.FitnessCenter
    ),
    Profile(
        route = "profile",
        titleRes = R.string.nav_profile,
        icon = Icons.Filled.Person,
        actions = listOf(
            TopBarAction(
                icon = Icons.Outlined.Edit,
                contentDescriptionRes = R.string.action_notifications
            ),
            TopBarAction(
                icon = Icons.Outlined.Settings,
                contentDescriptionRes = R.string.action_more
            )
        )
    )
}