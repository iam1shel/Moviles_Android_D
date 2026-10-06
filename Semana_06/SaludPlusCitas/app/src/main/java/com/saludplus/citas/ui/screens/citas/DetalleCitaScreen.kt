package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO reto: AlertDialog, cancelarCita
@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onVolver: () -> Unit
) {
    PantallaEnConstruccion(titulo = "Detalle cita #$citaId", onContinuar = onVolver, textoBoton = "Volver")
}
