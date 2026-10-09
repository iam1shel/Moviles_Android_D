package com.saludplus.citas.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Esquema = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    secondary = AzulClaro,
    onSecondary = AzulTexto,
    background = Color.White,
    onBackground = AzulTexto,
    surface = Color.White,
    onSurface = AzulTexto,
    onSurfaceVariant = AzulGris,
    outline = GrisBorde
)

private val Formas = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun SaludPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Esquema,
        typography = SaludPlusTypography,
        shapes = Formas,
        content = content
    )
}
