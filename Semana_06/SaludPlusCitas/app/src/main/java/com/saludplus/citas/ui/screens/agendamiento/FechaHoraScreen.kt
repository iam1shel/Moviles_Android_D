package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaHoraScreen(
    especialidadId: Int,
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    val dias = Repositorio.diasFijosFase1()
    var fecha by remember { mutableStateOf(dias.first()) }
    var hora by remember { mutableStateOf<String?>(null) }
    val horarios = Repositorio.horariosDisponibles(medicoId, fecha)
    val medico = Repositorio.obtenerMedico(medicoId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(medico?.nombre ?: "Fecha y hora") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Elige un día", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(dias) { dia ->
                    FilterChip(
                        selected = fecha == dia,
                        onClick = {
                            fecha = dia
                            hora = null
                        },
                        label = { Text(dia.takeLast(5)) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(horarios) { h ->
                    Surface(
                        onClick = { hora = h },
                        border = BorderStroke(
                            1.dp,
                            if (hora == h) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline
                        ),
                        shape = MaterialTheme.shapes.medium,
                        color = if (hora == h) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = h,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
            Button(
                onClick = { onContinuar(fecha, hora!!) },
                enabled = hora != null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continuar") }
        }
    }
}
