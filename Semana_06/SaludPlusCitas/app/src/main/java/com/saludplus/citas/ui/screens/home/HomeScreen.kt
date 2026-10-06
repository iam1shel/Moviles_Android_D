package com.saludplus.citas.ui.screens.home

import androidx.compose.runtime.Composable
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// TODO: Scaffold, NavigationBar, LazyRow destacadas
@Composable
fun HomeScreen(
    onEspecialidades: () -> Unit,
    onMisCitas: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit,
    onNotificaciones: () -> Unit
) {
    PantallaEnConstruccion(titulo = "Inicio", onContinuar = onEspecialidades)
}
