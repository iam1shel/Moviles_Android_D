package com.saludplus.citas.ui.screens.resultados

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO reto: lista fija
@Composable
fun ResultadosScreen(onVolver: () -> Unit) {
    PantallaEnConstruccion(titulo = "Resultados", onContinuar = onVolver, textoBoton = "Volver")
}
