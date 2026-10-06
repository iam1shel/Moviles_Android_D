package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: LazyVerticalGrid, horariosDisponibles, días fijos en Fase 1
@Composable
fun FechaHoraScreen(
    especialidadId: Int,
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Fecha y hora",
        onContinuar = { onContinuar("2026-10-10", "09:00") }
    )
}
