package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: parámetro especialidadId, filter + sortedByDescending
@Composable
fun MedicosScreen(
    especialidadId: Int,
    onVolver: () -> Unit,
    onSeleccionar: (Int) -> Unit
) {
    PantallaEnConstruccion(titulo = "Médicos ($especialidadId)", onContinuar = { onSeleccionar(1) })
}
