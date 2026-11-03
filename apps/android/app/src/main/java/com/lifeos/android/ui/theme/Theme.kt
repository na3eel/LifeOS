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
import com.santi020k.lumen.LumenTheme
import com.santi020k.lumen.LumenThemeValues
import com.santi020k.lumen.LumenSurfaceMaterial

// Strata is the single source of truth for both the Lumen primitives and
// Material 3 components used by the native Android screens.
private val StrataCanvas = Color(0xFFFFFEFC)
private val StrataSurface = Color(0xFFFFFFFF)
private val StrataInk = Color(0xFF171313)
private val StrataOrange = Color(0xFFDB5635)
private val StrataOrangeDeep = Color(0xFFB83F25)
private val StrataOrangeSoft = Color(0xFFFCE4DA)
private val StrataPurple = Color(0xFF9828F6)
private val StrataLine = Color(0xFFE9DDD6)
private val StrataMutedInk = Color(0xFF6F626B)

private val StrataLightMaterial = lightColorScheme(
    primary = StrataOrange,
    onPrimary = Color.White,
    primaryContainer = StrataOrangeSoft,
    onPrimaryContainer = StrataInk,
    secondary = StrataPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E8F8),
    onSecondaryContainer = StrataInk,
    background = StrataCanvas,
    onBackground = StrataInk,
    surface = StrataSurface,
    onSurface = StrataInk,
    surfaceVariant = Color(0xFFF3E8E1),
    onSurfaceVariant = StrataMutedInk,
    outline = Color(0xFFD6C5BC),
    outlineVariant = StrataLine,
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val StrataDarkMaterial = darkColorScheme(
    primary = Color(0xFFFF9A7E),
    onPrimary = Color(0xFF32130B),
    primaryContainer = Color(0xFF642B1C),
    onPrimaryContainer = Color(0xFFFFDAD0),
    secondary = Color(0xFFC99BFF),
    onSecondary = Color(0xFF35105C),
    secondaryContainer = Color(0xFF4B2670),
    onSecondaryContainer = Color(0xFFEEDBFF),
    background = Color(0xFF171313),
    onBackground = Color(0xFFF8F0F5),
    surface = Color(0xFF211C24),
    onSurface = Color(0xFFF8F0F5),
    surfaceVariant = Color(0xFF342B37),
    onSurfaceVariant = Color(0xFFB9AABF),
    outline = Color(0xFF655767),
    outlineVariant = Color(0xFF463A49),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val StrataLight = LumenColorPalette(
    canvas = StrataCanvas,
    surface = StrataSurface,
    surfaceMuted = Color(0xFFF3E8E1),
    surfaceStrong = StrataLine,
    line = Color(0xFFD6C5BC),
    ink = StrataInk,
    inkSoft = Color(0xFF443843),
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
    canvas = Color(0xFF171313),
    surface = Color(0xFF211C24),
    surfaceMuted = Color(0xFF342B37),
    surfaceStrong = Color(0xFF463A49),
    line = Color(0xFF655767),
    ink = Color(0xFFF8F0F5),
    inkSoft = Color(0xFFE0D5E3),
    inkMuted = Color(0xFFB9AABF),
    brand = Color(0xFFFF9A7E),
    brandSolid = Color(0xFFFF9A7E),
    brandSoft = Color(0xFF642B1C),
    onBrand = Color(0xFF32130B),
    accent = Color(0xFFC99BFF),
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
