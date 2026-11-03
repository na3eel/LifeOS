package com.lifeos.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.santi020k.lumen.LumenAppearance
import com.santi020k.lumen.LumenColorPalette
import com.santi020k.lumen.LumenTheme
import com.santi020k.lumen.LumenThemeValues
import com.santi020k.lumen.LumenSurfaceMaterial

/** LifeOS Strata semantic palette mapped into Lumen's native theme contract. */
private val StrataLight = LumenColorPalette(
    canvas = Color(0xFFFFFBF7),
    surface = Color(0xFFFFFFFF),
    surfaceMuted = Color(0xFFF3E8E1),
    surfaceStrong = Color(0xFFE9DDD6),
    line = Color(0xFFD6C5BC),
    ink = Color(0xFF1F1724),
    inkSoft = Color(0xFF443843),
    inkMuted = Color(0xFF6F626B),
    brand = Color(0xFFDB5635),
    brandSolid = Color(0xFFB83F25),
    brandSoft = Color(0xFFFCE4DA),
    onBrand = Color.White,
    accent = Color(0xFF9828F6),
    success = Color(0xFF247A4B),
    warning = Color(0xFF946000),
    danger = Color(0xFFB3261E),
    onDanger = Color.White
)

private val StrataDark = LumenColorPalette(
    canvas = Color(0xFF17131A),
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
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
)

@Composable
fun LifeOSTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
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
