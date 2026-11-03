package com.lifeos.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
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

// Tirni light tokens — keep these exact values aligned with docs/design/lifeos-approved-theme.md.
private val TirniCanvas = Color(0xFFFFFFFF)
private val TirniSurface = Color(0xFFFFF3F5)
private val TirniSurfaceStrong = Color(0xFFFFE1E8)
private val TirniInk = Color(0xFF111827)
private val TirniOrange = Color(0xFFE5093B)
private val TirniOrangeDeep = Color(0xFF8B001C)
private val TirniOrangeSoft = Color(0xFFFFE1E8)
private val TirniPurple = Color(0xFF8B001C)
private val TirniPurpleSoft = Color(0xFFFFE1E8)
private val TirniLine = Color(0xFFF1DDE2)
private val TirniMutedInk = Color(0xFF6B7280)

private val TirniDarkCanvas = Color(0xFF0F0B0D)
private val TirniDarkSurface = Color(0xFF1B1217)
private val TirniDarkSurfaceMuted = Color(0xFF291820)
private val TirniDarkSurfaceStrong = Color(0xFF3B202A)
private val TirniDarkInk = Color(0xFFFFF5F7)
private val TirniDarkMutedInk = Color(0xFFC8B8BE)
private val TirniDarkLine = Color(0xFF5A3A45)
private val TirniDarkOrange = Color(0xFFFF6B86)
private val TirniDarkOrangeSoft = Color(0xFF54202D)
private val TirniDarkPurple = Color(0xFFFFA3B6)
private val TirniDarkPurpleSoft = Color(0xFF54202D)

private val TirniLightMaterial = lightColorScheme(
    primary = TirniOrange,
    onPrimary = Color.White,
    primaryContainer = TirniOrangeSoft,
    onPrimaryContainer = TirniInk,
    secondary = TirniPurple,
    onSecondary = Color.White,
    secondaryContainer = TirniPurpleSoft,
    onSecondaryContainer = TirniInk,
    background = TirniCanvas,
    onBackground = TirniInk,
    surface = TirniSurface,
    onSurface = TirniInk,
    surfaceVariant = TirniSurface,
    onSurfaceVariant = TirniMutedInk,
    surfaceTint = TirniOrange,
    surfaceDim = TirniSurfaceStrong,
    surfaceBright = TirniCanvas,
    surfaceContainerLowest = TirniCanvas,
    surfaceContainerLow = TirniSurface,
    surfaceContainer = TirniSurface,
    surfaceContainerHigh = TirniSurfaceStrong,
    surfaceContainerHighest = TirniSurfaceStrong,
    outline = TirniLine,
    outlineVariant = TirniLine,
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val TirniDarkMaterial = darkColorScheme(
    primary = TirniDarkOrange,
    onPrimary = Color(0xFF32130B),
    primaryContainer = TirniDarkOrangeSoft,
    onPrimaryContainer = Color(0xFFFFDAD0),
    secondary = TirniDarkPurple,
    onSecondary = Color(0xFF35105C),
    secondaryContainer = TirniDarkPurpleSoft,
    onSecondaryContainer = Color(0xFFEEDBFF),
    background = TirniDarkCanvas,
    onBackground = TirniDarkInk,
    surface = TirniDarkSurface,
    onSurface = TirniDarkInk,
    surfaceVariant = TirniDarkSurfaceMuted,
    onSurfaceVariant = TirniDarkMutedInk,
    surfaceTint = TirniDarkOrange,
    surfaceDim = TirniDarkCanvas,
    surfaceBright = TirniDarkSurfaceStrong,
    surfaceContainerLowest = TirniDarkCanvas,
    surfaceContainerLow = TirniDarkSurface,
    surfaceContainer = TirniDarkSurface,
    surfaceContainerHigh = TirniDarkSurfaceMuted,
    surfaceContainerHighest = TirniDarkSurfaceStrong,
    outline = TirniDarkLine,
    outlineVariant = TirniDarkLine,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val TirniLight = LumenColorPalette(
    canvas = TirniCanvas,
    surface = TirniSurface,
    surfaceMuted = TirniSurface,
    surfaceStrong = TirniSurfaceStrong,
    line = TirniLine,
    ink = TirniInk,
    inkSoft = Color(0xFF443843),
    inkMuted = TirniMutedInk,
    brand = TirniOrange,
    brandSolid = TirniOrangeDeep,
    brandSoft = TirniOrangeSoft,
    onBrand = Color.White,
    accent = TirniPurple,
    success = Color(0xFF247A4B),
    warning = Color(0xFF946000),
    danger = Color(0xFFB3261E),
    onDanger = Color.White
)

private val TirniDark = LumenColorPalette(
    canvas = TirniDarkCanvas,
    surface = TirniDarkSurface,
    surfaceMuted = TirniDarkSurfaceMuted,
    surfaceStrong = TirniDarkSurfaceStrong,
    line = TirniDarkLine,
    ink = TirniDarkInk,
    inkSoft = Color(0xFFE0D5E3),
    inkMuted = TirniDarkMutedInk,
    brand = TirniDarkOrange,
    brandSolid = TirniDarkOrange,
    brandSoft = TirniDarkOrangeSoft,
    onBrand = Color(0xFF32130B),
    accent = TirniDarkPurple,
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
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) TirniDarkMaterial else TirniLightMaterial,
        shapes = LifeOSShapes
    ) {
        LumenTheme(
            darkTheme = dark,
            values = LumenThemeValues(
                colors = if (dark) TirniDark else TirniLight,
                isDark = dark,
                appearance = LumenAppearance(material = LumenSurfaceMaterial.Solid)
            ),
            shapes = LifeOSShapes,
            content = content
        )
    }
}
