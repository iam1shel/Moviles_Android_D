package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO reto: scroll o AlertDialog
@Composable
fun TerminosScreen(onVolver: () -> Unit) {
    PantallaEnConstruccion(titulo = "Términos", onContinuar = onVolver, textoBoton = "Volver")
}
