package com.apppillar.feature_profile

import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class DayAxisFormatter(private val startDate: LocalDate) : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val index = value.toInt().coerceIn(0, 6)
        val date = startDate.plusDays(index.toLong())
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru"))
        val day = date.format(DateTimeFormatter.ofPattern("dd.MM"))
        return "$dayOfWeek\n$day"
    }
}