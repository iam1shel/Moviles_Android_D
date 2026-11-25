package com.tecsup.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresMuroCampus = lightColorScheme(
    primary = Color(0xFF196996),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4EAF8),
    onPrimaryContainer = Color(0xFF0D4E72),
    background = Color(0xFFFAF8FC),
    surface = Color(0xFFFAF8FC),
    surfaceVariant = Color(0xFFEDF1F7),
    errorContainer = Color(0xFFFBE4E4),
    onErrorContainer = Color(0xFF8C2929)
)

@Composable
fun MuroCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresMuroCampus, content = content)
}
