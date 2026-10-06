package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO reto: map sobre citas
@Composable
fun NotificacionesScreen(onVolver: () -> Unit) {
    PantallaEnConstruccion(titulo = "Notificaciones", onContinuar = onVolver, textoBoton = "Volver")
}
