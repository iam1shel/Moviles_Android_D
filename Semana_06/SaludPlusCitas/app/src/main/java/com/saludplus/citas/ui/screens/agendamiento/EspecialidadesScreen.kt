package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: LazyColumn + búsqueda filter
@Composable
fun EspecialidadesScreen(
    onVolver: () -> Unit,
    onSeleccionar: (Int) -> Unit
) {
    PantallaEnConstruccion(titulo = "Especialidades", onContinuar = { onSeleccionar(1) })
}
