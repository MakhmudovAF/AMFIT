package com.example.amfit.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.amfit.R
import com.example.amfit.data.TopBarAction
import com.example.amfit.ui.icons.edit
import com.example.amfit.ui.icons.exercise
import com.example.amfit.ui.icons.home
import com.example.amfit.ui.icons.more_vert
import com.example.amfit.ui.icons.notifications
import com.example.amfit.ui.icons.person
import com.example.amfit.ui.icons.settings

enum class Screen(
    val route: String,
    val titleRes: Int,
    val icon: ImageVector,
    val actions: List<TopBarAction> = emptyList()
) {
    Home(
        route = "home",
        titleRes = R.string.nav_home,
        icon = home,
        actions = listOf(
            TopBarAction(
                icon = notifications,
                contentDescriptionRes = R.string.action_notifications
            ),
            TopBarAction(
                icon = more_vert,
                contentDescriptionRes = R.string.action_more
            )
        )
    ),
    Workout(
        route = "workout",
        titleRes = R.string.nav_workout,
        icon = exercise
    ),
    Profile(
        route = "profile",
        titleRes = R.string.nav_profile,
        icon = person,
        actions = listOf(
            TopBarAction(
                icon = edit,
                contentDescriptionRes = R.string.action_notifications
            ),
            TopBarAction(
                icon = settings,
                contentDescriptionRes = R.string.action_more
            )
        )
    )
}