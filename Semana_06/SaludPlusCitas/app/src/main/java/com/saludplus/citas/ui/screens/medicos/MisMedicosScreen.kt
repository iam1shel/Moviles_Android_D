package com.saludplus.citas.ui.screens.medicos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.FilaMedicoLista
import com.saludplus.citas.ui.components.SaludPlusTopBar
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.GrisFondo

@Composable
fun MisMedicosScreen(onVolver: () -> Unit) {
    val grupos = remember { Repositorio.medicosAgrupadosPorEspecialidad() }

    Scaffold(
        containerColor = GrisFondo,
        topBar = {
            SaludPlusTopBar(title = "Mis Médicos", onVolver = onVolver)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            grupos.forEach { grupo ->
                item(key = "esp-${grupo.especialidad.id}") {
                    Text(
                        text = grupo.especialidad.nombre,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(grupo.medicos, key = { it.id }) { medico ->
                    FilaMedicoLista(
                        medico = medico,
                        especialidad = grupo.especialidad.nombre,
                        onClick = {}
                    )
                }
            }
        }
    }
}
