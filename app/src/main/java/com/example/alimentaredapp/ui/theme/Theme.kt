package com.example.alimentaredapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDarkTheme,
    onPrimary = Color.Black,
    primaryContainer = GreenDark,
    onPrimaryContainer = GreenPrimaryContainer,

    secondary = OrangeSecondary,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF6E2C00),
    onSecondaryContainer = OrangeSecondaryContainer,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFB0B0B0),

    error = Color(0xFFE57373),
    onError = Color.Black
)

@Composable
fun AlimentaRedAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Forzamos false por defecto para respetar los colores de marca del mockup
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
