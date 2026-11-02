package com.lifeos.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// LifeOS identity: orange + purple, grounded by white and near-black.
private val BrandOrange = Color(0xFFDB5635)
private val GrowthPurple = Color(0xFF9828F6)
private val WarmWhite = Color(0xFFFFFEFC)
private val NearBlack = Color(0xFF171313)

private val LightColors = lightColorScheme(
    primary = BrandOrange,
    onPrimary = WarmWhite,
    secondary = GrowthPurple,
    onSecondary = WarmWhite,
    tertiary = GrowthPurple,
    background = WarmWhite,
    onBackground = NearBlack,
    surface = WarmWhite,
    onSurface = NearBlack,
    surfaceVariant = Color(0xFFF2ECF5),
    onSurfaceVariant = Color(0xFF514852),
    outline = Color(0xFF817781)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF8A68),
    onPrimary = NearBlack,
    secondary = Color(0xFFC18BFF),
    onSecondary = NearBlack,
    tertiary = Color(0xFFC18BFF),
    background = NearBlack,
    onBackground = WarmWhite,
    surface = Color(0xFF211C20),
    onSurface = WarmWhite,
    surfaceVariant = Color(0xFF342B37),
    onSurfaceVariant = Color(0xFFE0D5E3),
    outline = Color(0xFF9A8D9D)
)

@Composable
fun LifeOSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}
