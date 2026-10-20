package com.tecsup.huellitasvet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.Rol

private val DuenoLight = lightColorScheme(
    primary = Esmeralda,
    onPrimary = Color.White,
    primaryContainer = EsmeraldaContenedor,
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Esmeralda,
    onSecondary = Color.White,
    secondaryContainer = EsmeraldaContenedor,
    background = EsmeraldaFondo,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = EsmeraldaContenedor,
    onSurfaceVariant = TextoSecundario,
    error = CanceladaColor
)

private val VetLight = lightColorScheme(
    primary = AzulVet,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = AzulVet,
    onSecondary = Color.White,
    secondaryContainer = AzulContenedor,
    background = AzulFondo,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = AzulContenedor,
    onSurfaceVariant = TextoSecundario,
    error = CanceladaColor
)

private val DuenoDark = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF064E3B),
    primaryContainer = Color(0xFF065F46),
    onPrimaryContainer = Color(0xFFD1FAE5),
    background = Color(0xFF0B1F17),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF12261D),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF1F3D32),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = Color(0xFFF87171)
)

private val VetDark = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF1E3A8A),
    primaryContainer = Color(0xFF1E40AF),
    onPrimaryContainer = Color(0xFFDBEAFE),
    background = Color(0xFF0B1220),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF1E3A5F),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = Color(0xFFF87171)
)

@Composable
fun VetTheme(
    rol: Rol = Rol.CLIENTE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val scheme = when {
        rol == Rol.ADMIN && darkTheme -> VetDark
        rol == Rol.ADMIN -> VetLight
        darkTheme -> DuenoDark
        else -> DuenoLight
    }
    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content
    )
}
