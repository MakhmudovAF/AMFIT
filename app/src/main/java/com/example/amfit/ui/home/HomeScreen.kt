package com.example.amfit.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amfit.ui.components.HomeHeaderSection
import com.example.amfit.ui.components.WorkoutCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            HomeHeaderSection(
                uiState = uiState,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(
            items = uiState.finishedWorkouts,
            key = { workout -> workout.id }
        ) { workout ->

            WorkoutCard(
                finishedWorkout = workout,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
