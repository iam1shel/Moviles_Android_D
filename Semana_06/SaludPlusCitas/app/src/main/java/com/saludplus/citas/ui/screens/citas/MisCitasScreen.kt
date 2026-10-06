package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: LazyColumn, lista vacía
@Composable
fun MisCitasScreen(
    onVolver: () -> Unit,
    onDetalle: (Int) -> Unit
) {
    PantallaEnConstruccion(titulo = "Mis citas", onContinuar = onVolver, textoBoton = "Volver")
}
