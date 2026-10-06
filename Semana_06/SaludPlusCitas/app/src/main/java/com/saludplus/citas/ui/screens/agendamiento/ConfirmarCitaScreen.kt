package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: 3 parámetros, agendarCita
@Composable
fun ConfirmarCitaScreen(
    especialidadId: Int,
    medicoId: Int,
    fecha: String,
    hora: String,
    onVolver: () -> Unit,
    onConfirmado: (citaId: Int) -> Unit
) {
    PantallaEnConstruccion(titulo = "Confirmar", onContinuar = { onConfirmado(1) })
}
