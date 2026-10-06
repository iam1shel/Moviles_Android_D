package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: popUpTo, resumen
@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onMisCitas: () -> Unit,
    onInicio: () -> Unit
) {
    PantallaEnConstruccion(titulo = "Cita agendada #$citaId", onContinuar = onMisCitas)
}
