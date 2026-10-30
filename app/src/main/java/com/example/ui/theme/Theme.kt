package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    primaryContainer = DeepViolet,
    onPrimaryContainer = Color.White,
    secondary = HakiGold,
    onSecondary = Color.Black,
    tertiary = CrimsonHaki,
    background = ArenaBackground,
    onBackground = TextPrimary,
    surface = ArenaSurface,
    onSurface = TextPrimary,
    surfaceVariant = ArenaSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = ArenaCardBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = ArenaTypography,
        content = content
    )
}
