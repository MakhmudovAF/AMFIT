package com.example.amfit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amfit.ui.home.HomeUiState

@Composable
fun HomeStats(
    uiState: HomeUiState,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HomeStatsIndicator(
                title = "Daily Steps",
                valueText = "${uiState.dailySteps}/${uiState.dailyStepsGoal}",
                progress = uiState.dailySteps.toFloat() /
                        uiState.dailyStepsGoal.toFloat(),
                modifier = Modifier.weight(1f)
            )
            HomeStatsIndicator(
                title = "Day Working Out",
                valueText = "${uiState.workoutDays}/${uiState.workoutDaysGoal}",
                progress = uiState.workoutDays.toFloat() /
                        uiState.workoutDaysGoal.toFloat(),
                modifier = Modifier.weight(1f)
            )
            HomeStatsIndicator(
                title = "Calories Burnt",
                valueText = "${uiState.caloriesBurnt}/${uiState.caloriesGoal}",
                progress = uiState.caloriesBurnt.toFloat() /
                        uiState.caloriesGoal.toFloat(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HomeStatsIndicator(
    title: String,
    valueText: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = {
                progress.coerceIn(0f, 1f)
            }
        )
        Text(
            text = valueText,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = title,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
