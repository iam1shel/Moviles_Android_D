package com.saludplus.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Esquema = lightColorScheme(
    primary = AzulSalud,
    onPrimary = Color.White,
    secondary = AzulClaro,
    background = GrisFondo,
    onBackground = TextoOscuro,
    surface = Color.White,
    onSurface = TextoOscuro
)

@Composable
fun SaludPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Esquema,
        typography = SaludPlusTypography,
        content = content
    )
}
