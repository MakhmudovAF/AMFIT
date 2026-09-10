package com.example.amfit.data

import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarAction(
    val icon: ImageVector,
    val contentDescriptionRes: Int,
    val onClick: () -> Unit = {}
)