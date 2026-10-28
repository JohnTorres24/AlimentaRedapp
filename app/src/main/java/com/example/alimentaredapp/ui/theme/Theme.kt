package com.example.alimentaredapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenPrimaryContainer,
    onPrimaryContainer = GreenDark,

    secondary = OrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = OrangeSecondaryContainer,
    onSecondaryContainer = Color(0xFF6E2C00),

    tertiary = BlueTertiary,
    onTertiary = Color.White,
    tertiaryContainer = BlueTertiaryContainer,
    onTertiaryContainer = Color(0xFF1B4F72),

    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,

    error = StatusExpired,
    onError = Color.White,
    errorContainer = Color(0xFFFDEDEC),
    onErrorContainer = Color(0xFF78281F)
)

@Composable
fun AlimentaRedAppTheme(
    darkTheme: Boolean = false, // Forzado siempre a tema blanco tal cual los Mockups de Figma
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
