package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: estados, OutlinedTextField, validaciones, registrarUsuario
@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onRegistrado: () -> Unit,
    onTerminos: () -> Unit
) {
    PantallaEnConstruccion(titulo = "Registro", onContinuar = onRegistrado)
}
