package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color(0xFF003822),
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = EmeraldOnContainerDark,
    secondary = EmeraldLight,
    onSecondary = Color(0xFF003822),
    secondaryContainer = NavySurfaceCardElevated,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = GoldAmber,
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = GoldAmberContainer,
    onTertiaryContainer = Color(0xFFFDE68A),
    background = NavyBackground,
    onBackground = DarkTextPrimary,
    surface = NavySurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = NavySurfaceCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = NavyBorder,
    outlineVariant = NavyDivider,
    error = RoseSoft,
    onError = Color.White,
    errorContainer = RoseSoftContainer,
    onErrorContainer = Color(0xFFFFD1D8)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = EmeraldPrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = LightTextPrimary,
    tertiary = GoldAmber,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF78350F),
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceCard,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFCBD5E1),
    error = RoseSoft,
    onError = Color.White
)

@Composable
fun StopMasturbateTheme(
    darkTheme: Boolean = true, // Default to sleek deep navy / dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
