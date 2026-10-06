package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: diseño splash con Image, Column y botones
@Composable
fun SplashScreen(
    onRegistrarse: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Splash",
        onContinuar = onRegistrarse,
        textoBoton = "Ir a registro"
    )
}
