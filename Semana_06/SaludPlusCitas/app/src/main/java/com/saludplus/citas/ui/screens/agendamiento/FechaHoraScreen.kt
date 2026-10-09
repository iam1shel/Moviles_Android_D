package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaHoraScreen(
    especialidadId: Int,
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    var semanaInicio by remember { mutableStateOf(CalendarioUtils.lunesDeSemana(LocalDate.now())) }
    val dias = remember(semanaInicio) { CalendarioUtils.diasHabilesDeSemana(semanaInicio) }
    var fechaSeleccionada by remember {
        mutableStateOf(CalendarioUtils.toIso(dias.first()))
    }
    var hora by remember { mutableStateOf<String?>(null) }

    // Si la semana cambia y el día elegido ya no está, reinicia selección.
    if (dias.none { CalendarioUtils.toIso(it) == fechaSeleccionada }) {
        fechaSeleccionada = CalendarioUtils.toIso(dias.first())
        hora = null
    }

    val horarios = Repositorio.horariosDisponibles(medicoId, fechaSeleccionada)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        if (CalendarioUtils.puedeRetroceder(semanaInicio)) {
                            semanaInicio = semanaInicio.minusWeeks(1)
                            hora = null
                        }
                    },
                    enabled = CalendarioUtils.puedeRetroceder(semanaInicio)
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                }
                Text(
                    text = CalendarioUtils.tituloMesAnio(semanaInicio),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        semanaInicio = semanaInicio.plusWeeks(1)
                        hora = null
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Elige un día hábil", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(dias, key = { it.toString() }) { dia ->
                    val iso = CalendarioUtils.toIso(dia)
                    FilterChip(
                        selected = fechaSeleccionada == iso,
                        onClick = {
                            fechaSeleccionada = iso
                            hora = null
                        },
                        label = { Text(CalendarioUtils.etiquetaCorta(dia)) }
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
                        Text(text = h, modifier = Modifier.padding(12.dp))
                    }
                }
            }
            Button(
                onClick = { onContinuar(fechaSeleccionada, hora!!) },
                enabled = hora != null,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continuar") }
        }
    }
}
