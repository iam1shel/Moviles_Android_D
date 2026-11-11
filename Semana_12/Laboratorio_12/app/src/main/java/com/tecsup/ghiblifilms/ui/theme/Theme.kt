package com.tecsup.ghiblifilms.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Claro = lightColorScheme(
    primary = CieloLaputa,
    onPrimary = Color.White,
    primaryContainer = CieloClaro,
    onPrimaryContainer = Tinta,
    secondary = BosqueTotoro,
    onSecondary = Color.White,
    secondaryContainer = BosqueClaro,
    onSecondaryContainer = Tinta,
    background = Crema,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = CieloClaro,
    onSurfaceVariant = TintaSuave,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8FAFD),
    surfaceContainer = Color(0xFFF2F6FA),
    surfaceContainerHigh = Color(0xFFECF2F8),
    surfaceContainerHighest = Color(0xFFE5EDF5),
    error = RojoError
)

private val Oscuro = darkColorScheme(
    primary = CieloNoche,
    onPrimary = Tinta,
    primaryContainer = CieloLaputa,
    onPrimaryContainer = Color.White,
    secondary = BosqueNoche,
    onSecondary = Tinta,
    secondaryContainer = BosqueTotoro,
    onSecondaryContainer = Color.White,
    background = FondoNoche,
    onBackground = Color(0xFFE2E8F0),
    surface = SuperficieNoche,
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF26364A),
    onSurfaceVariant = Color(0xFF94A3B8),
    surfaceContainerLowest = FondoNoche,
    surfaceContainerLow = Color(0xFF18212C),
    surfaceContainer = SuperficieNoche,
    surfaceContainerHigh = Color(0xFF212C39),
    surfaceContainerHighest = Color(0xFF283543),
    error = Color(0xFFF87171)
)

@Composable
fun GhibliTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) Oscuro else Claro,
        typography = Typography,
        content = content
    )
}
