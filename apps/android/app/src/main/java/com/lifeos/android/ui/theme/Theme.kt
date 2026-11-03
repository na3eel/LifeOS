package com.lifeos.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** LifeOS Strata tokens. Keep in sync with docs/design/lifeos-approved-theme.md. */
private val BrandOrange = Color(0xFFDB5635)
private val GrowthPurple = Color(0xFF9828F6)
private val WarmCanvas = Color(0xFFFFFBF7)
private val WarmSurface = Color(0xFFF3E8E1)
private val WarmBorder = Color(0xFFD6C5BC)
private val NearBlack = Color(0xFF1F1724)
private val MutedInk = Color(0xFF6F626B)

private val LightColors = lightColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFCE4DA),
    onPrimaryContainer = NearBlack,
    secondary = GrowthPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0E4FA),
    onSecondaryContainer = NearBlack,
    tertiary = GrowthPurple,
    background = WarmCanvas,
    onBackground = NearBlack,
    surface = WarmCanvas,
    onSurface = NearBlack,
    surfaceVariant = WarmSurface,
    onSurfaceVariant = MutedInk,
    outline = WarmBorder,
    outlineVariant = Color(0xFFE9DDD6),
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF9A7E),
    onPrimary = Color(0xFF32130B),
    primaryContainer = Color(0xFF642B1C),
    onPrimaryContainer = Color(0xFFFFE1D7),
    secondary = Color(0xFFC99BFF),
    onSecondary = Color(0xFF321052),
    secondaryContainer = Color(0xFF48266A),
    onSecondaryContainer = Color(0xFFF0DFFF),
    tertiary = Color(0xFFC99BFF),
    background = Color(0xFF17131A),
    onBackground = Color(0xFFF8F0F5),
    surface = Color(0xFF211C24),
    onSurface = Color(0xFFF8F0F5),
    surfaceVariant = Color(0xFF342B37),
    onSurfaceVariant = Color(0xFFE0D5E3),
    outline = Color(0xFF8F808D),
    outlineVariant = Color(0xFF514652),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LifeOSShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
)

@Composable
fun LifeOSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        shapes = LifeOSShapes,
        content = content
    )
}
