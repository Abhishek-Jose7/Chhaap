package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ChhaapLightColorScheme = lightColorScheme(
    primary = ChhaapOrange,
    onPrimary = Color.White,
    primaryContainer = ChhaapOrangeSoft,
    onPrimaryContainer = ChhaapOrangeDark,
    secondary = DeepNavy,
    onSecondary = Color.White,
    secondaryContainer = SurfaceSubtle,
    onSecondaryContainer = DeepNavy,
    tertiary = VerifiedGreen,
    onTertiary = Color.White,
    tertiaryContainer = VerifiedGreenSoft,
    onTertiaryContainer = VerifiedGreenDark,
    background = SurfaceBg,
    onBackground = CharcoalInk,
    surface = SurfaceWhite,
    onSurface = CharcoalInk,
    surfaceVariant = SurfaceSubtle,
    onSurfaceVariant = SlateMuted,
    outline = BorderHairline,
    outlineVariant = BorderStrong,
    error = AlertRed,
    onError = Color.White,
    errorContainer = AlertRedSoft,
    onErrorContainer = AlertRed
)

val ChhaapShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun ChhaapTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ChhaapLightColorScheme,
        typography = Typography,
        shapes = ChhaapShapes,
        content = content
    )
}
