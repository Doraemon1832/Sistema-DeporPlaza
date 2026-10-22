package com.example.tucancha.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DeporYellowPrimary,
    onPrimary = ChipSelectedText,
    primaryContainer = DeporYellowContainer,
    onPrimaryContainer = DeporYellowText,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCardBorder,
    onSurfaceVariant = TextSecondary
)

@Composable
fun TuCanchaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
