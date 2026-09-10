package com.example.amfit.ui.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amfit.R
import com.example.amfit.ui.screens.home.HomeUiState

@Composable
fun HomeHeaderSection(
    uiState: HomeUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(
                R.string.home_greeting,
                uiState.userName
            )
        )
        Text(
            text = stringResource(R.string.home_welcome),
            modifier = Modifier.padding(top = 8.dp)
        )
        HomeStats(
            uiState = uiState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        HomeYourWeekSection(
            days = uiState.weekDays,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Text(
            text = stringResource(R.string.home_latest_workout),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
