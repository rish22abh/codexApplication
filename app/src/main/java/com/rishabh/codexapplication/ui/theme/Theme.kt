package com.rishabh.codexapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    background = Color(0xFFF4F1F8),
    surface = Color(0xFFFFFBFE),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFFECE6F0),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0)
)

@Composable
fun CodexApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
