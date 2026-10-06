package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: iniciarSesion con find sobre usuarios
@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onLoginOk: () -> Unit
) {
    PantallaEnConstruccion(titulo = "Login", onContinuar = onLoginOk)
}
