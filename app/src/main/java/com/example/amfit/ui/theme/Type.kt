package com.example.amfit.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.amfit.R

val RobotoFontFamily = FontFamily(
    Font(resId = R.font.roboto_light, weight = FontWeight.Light),
    Font(resId = R.font.roboto_regular, weight = FontWeight.Normal),
    Font(resId = R.font.roboto_medium, weight = FontWeight.Medium),
    Font(resId = R.font.roboto_semibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.roboto_bold, weight = FontWeight.Bold)
)

private val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = RobotoFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = RobotoFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = RobotoFontFamily),

    headlineLarge = baseline.headlineLarge.copy(fontFamily = RobotoFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = RobotoFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = RobotoFontFamily),

    titleLarge = baseline.titleLarge.copy(fontFamily = RobotoFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = RobotoFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = RobotoFontFamily),

    bodyLarge = baseline.bodyLarge.copy(fontFamily = RobotoFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = RobotoFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = RobotoFontFamily),

    labelLarge = baseline.labelLarge.copy(fontFamily = RobotoFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = RobotoFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = RobotoFontFamily)
)