package com.example.amfit.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.amfit.data.WeekDay

@Composable
fun HomeDayItem(
    day: WeekDay,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .then(
                    when {
                        day.isCompleted -> {
                            Modifier.background(primaryColor)
                        }

                        day.isCurrent -> {
                            Modifier.border(
                                width = 1.5.dp,
                                color = primaryColor,
                                shape = CircleShape
                            )
                        }

                        else -> {
                            Modifier.border(
                                width = 1.5.dp,
                                color = outlineColor,
                                shape = CircleShape
                            )
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.letter,
                color = when {
                    day.isCompleted ->
                        MaterialTheme.colorScheme.onPrimary

                    day.isCurrent ->
                        primaryColor

                    else ->
                        MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}
