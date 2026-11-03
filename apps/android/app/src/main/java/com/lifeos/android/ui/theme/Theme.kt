package com.lifeos.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.santi020k.lumen.LumenAppearance
import com.santi020k.lumen.LumenColorPalette
import com.santi020k.lumen.LumenSurfaceMaterial
import com.santi020k.lumen.LumenTheme
import com.santi020k.lumen.LumenThemeValues

// Strata light tokens — keep these exact values aligned with docs/design/lifeos-approved-theme.md.
private val StrataCanvas = Color(0xFFFFFBF7)
private val StrataSurface = Color(0xFFF3E8E1)
private val StrataSurfaceStrong = Color(0xFFFCE4DA)
private val StrataInk = Color(0xFF1F1724)
private val StrataOrange = Color(0xFFDB5635)
private val StrataOrangeDeep = Color(0xFFB83F25)
private val StrataOrangeSoft = Color(0xFFFCE4DA)
private val StrataPurple = Color(0xFF9828F6)
private val StrataPurpleSoft = Color(0xFFFCE4DA)
private val StrataLine = Color(0xFFD6C5BC)
private val StrataMutedInk = Color(0xFF6F626B)

private val StrataDarkCanvas = Color(0xFF171313)
private val StrataDarkSurface = Color(0xFF211C24)
private val StrataDarkSurfaceMuted = Color(0xFF342B37)
private val StrataDarkSurfaceStrong = Color(0xFF463A49)
private val StrataDarkInk = Color(0xFFF8F0F5)
private val StrataDarkMutedInk = Color(0xFFB9AABF)
private val StrataDarkLine = Color(0xFF655767)
private val StrataDarkOrange = Color(0xFFFF9A7E)
private val StrataDarkOrangeSoft = Color(0xFF642B1C)
private val StrataDarkPurple = Color(0xFFC99BFF)
private val StrataDarkPurpleSoft = Color(0xFF642B1C)

private val StrataLightMaterial = lightColorScheme(
    primary = StrataOrange,
    onPrimary = Color.White,
    primaryContainer = StrataOrangeSoft,
    onPrimaryContainer = StrataInk,
    secondary = StrataPurple,
    onSecondary = StrataInk,
    secondaryContainer = StrataPurpleSoft,
    onSecondaryContainer = StrataInk,
    background = StrataCanvas,
    onBackground = StrataInk,
    surface = StrataSurface,
    onSurface = StrataInk,
    surfaceVariant = StrataSurface,
    onSurfaceVariant = StrataMutedInk,
    surfaceTint = StrataOrange,
    surfaceDim = StrataSurfaceStrong,
    surfaceBright = StrataCanvas,
    surfaceContainerLowest = StrataCanvas,
    surfaceContainerLow = StrataSurface,
    surfaceContainer = StrataSurface,
    surfaceContainerHigh = StrataSurfaceStrong,
    surfaceContainerHighest = StrataSurfaceStrong,
    outline = StrataLine,
    outlineVariant = StrataLine,
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val StrataDarkMaterial = darkColorScheme(
    primary = StrataDarkOrange,
    onPrimary = Color(0xFF3B0715),
    primaryContainer = StrataDarkOrangeSoft,
    onPrimaryContainer = Color(0xFFFFE7EC),
    secondary = StrataDarkPurple,
    onSecondary = Color(0xFFFFE7EC),
    secondaryContainer = StrataDarkPurpleSoft,
    onSecondaryContainer = Color(0xFFFFE7EC),
    background = StrataDarkCanvas,
    onBackground = StrataDarkInk,
    surface = StrataDarkSurface,
    onSurface = StrataDarkInk,
    surfaceVariant = StrataDarkSurfaceMuted,
    onSurfaceVariant = StrataDarkMutedInk,
    surfaceTint = StrataDarkOrange,
    surfaceDim = StrataDarkCanvas,
    surfaceBright = StrataDarkSurfaceStrong,
    surfaceContainerLowest = StrataDarkCanvas,
    surfaceContainerLow = StrataDarkSurface,
    surfaceContainer = StrataDarkSurface,
    surfaceContainerHigh = StrataDarkSurfaceMuted,
    surfaceContainerHighest = StrataDarkSurfaceStrong,
    outline = StrataDarkLine,
    outlineVariant = StrataDarkLine,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val StrataLight = LumenColorPalette(
    canvas = StrataCanvas,
    surface = StrataSurface,
    surfaceMuted = StrataSurface,
    surfaceStrong = StrataSurfaceStrong,
    line = StrataLine,
    ink = StrataInk,
    inkSoft = Color(0xFF374151),
    inkMuted = StrataMutedInk,
    brand = StrataOrange,
    brandSolid = StrataOrangeDeep,
    brandSoft = StrataOrangeSoft,
    onBrand = Color.White,
    accent = StrataPurple,
    success = Color(0xFF247A4B),
    warning = Color(0xFF946000),
    danger = Color(0xFFB3261E),
    onDanger = Color.White
)

private val StrataDark = LumenColorPalette(
    canvas = StrataDarkCanvas,
    surface = StrataDarkSurface,
    surfaceMuted = StrataDarkSurfaceMuted,
    surfaceStrong = StrataDarkSurfaceStrong,
    line = StrataDarkLine,
    ink = StrataDarkInk,
    inkSoft = Color(0xFFE0D5E3),
    inkMuted = StrataDarkMutedInk,
    brand = StrataDarkOrange,
    brandSolid = StrataDarkOrange,
    brandSoft = StrataDarkOrangeSoft,
    onBrand = Color(0xFF3B0715),
    accent = StrataDarkPurple,
    success = Color(0xFF79D69D),
    warning = Color(0xFFF2C36D),
    danger = Color(0xFFFFB4AB),
    onDanger = Color(0xFF690005)
)

private val LifeOSShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun LifeOSTheme(content: @Composable () -> Unit) {
    val dark = false // Strata light appearance is the default; a user-facing appearance setting can opt into dark mode.
    MaterialTheme(
        colorScheme = if (dark) StrataDarkMaterial else StrataLightMaterial,
        shapes = LifeOSShapes
    ) {
        LumenTheme(
            darkTheme = dark,
            values = LumenThemeValues(
                colors = if (dark) StrataDark else StrataLight,
                isDark = dark,
                appearance = LumenAppearance(material = LumenSurfaceMaterial.Solid)
            ),
            shapes = LifeOSShapes,
            content = content
        )
    }
}
