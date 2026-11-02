package com.lifeos.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF087F8C),
    onPrimary = Color.White,
    background = Color(0xFFF7F9FA),
    surface = Color.White,
    onSurface = Color(0xFF1C252B),
    secondary = Color(0xFF168C78)
)
private val DarkColors = darkColorScheme(
    primary = Color(0xFF71D1D5),
    background = Color(0xFF101719),
    surface = Color(0xFF192326),
    onSurface = Color(0xFFE7EFF0)
)

@Composable
fun LifeOSTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors, content = content)
}
