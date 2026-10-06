package com.rojastuesta.tecsupstore.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Morado,
    onPrimary = Color.White,
    primaryContainer = Lavanda,
    onPrimaryContainer = MoradoOscuro,
    secondary = Morado,
    onSecondary = Color.White,
    secondaryContainer = Lavanda,
    onSecondaryContainer = MoradoOscuro,
    background = Fondo,
    onBackground = Texto,
    surface = Fondo,
    onSurface = Texto,
    surfaceVariant = LavandaSuave,
    onSurfaceVariant = TextoSecundario
)

@Composable
fun TecsupStoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
