package com.example.amfit.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.amfit.data.TopBarAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: List<TopBarAction>
) {
    TopAppBar(
        title = {
            Text(text = stringResource(title))
        },
        actions = {
            actions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = stringResource(action.contentDescriptionRes)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
