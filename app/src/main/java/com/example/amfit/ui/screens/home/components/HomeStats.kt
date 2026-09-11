package com.example.amfit.ui.screens.home.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amfit.R
import com.example.amfit.ui.screens.home.HomeUiState

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
                title = stringResource(R.string.stats_daily_steps),
                valueText = stringResource(
                    R.string.format_progress,
                    uiState.dailySteps,
                    uiState.dailyStepsGoal
                ),
                progress = if (uiState.dailyStepsGoal == 0) 0f else uiState.dailySteps.toFloat() / uiState.dailyStepsGoal,
                modifier = Modifier.weight(1f)
            )
            HomeStatsIndicator(
                title = stringResource(R.string.stats_week_streak),
                valueText = stringResource(
                    R.string.format_progress,
                    uiState.weekStreak,
                    uiState.weekStreakGoal
                ),
                progress = if (uiState.weekStreakGoal == 0) 0f else uiState.weekStreak.toFloat() / uiState.weekStreakGoal,
                modifier = Modifier.weight(1f)
            )
            HomeStatsIndicator(
                title = stringResource(R.string.stats_calories_burned),
                valueText = stringResource(
                    R.string.format_progress,
                    uiState.caloriesBurned,
                    uiState.caloriesGoal
                ),
                progress = if (uiState.caloriesGoal == 0) 0f else uiState.caloriesBurned.toFloat() / uiState.caloriesGoal,
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
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = title,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
