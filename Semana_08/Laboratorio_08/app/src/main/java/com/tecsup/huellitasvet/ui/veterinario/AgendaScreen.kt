package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate

@Composable
fun AgendaScreen(vm: AgendaViewModel, onAtender: (Int) -> Unit) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val dias = (0L..6L).map { LocalDate.now().plusDays(it) }

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Agenda")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dias) { d ->
                FechaChip(d, seleccionado = fecha == d.toString(), onClick = { vm.fechaSeleccionada.value = d.toString() })
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(citas, key = { it.cita.id }) { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(d.cita.hora, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(56.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            MascotaAvatar(d.mascota.mascota.especie, tamano = 48.dp, nombre = d.mascota.mascota.nombre)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text("${d.mascota.mascota.nombre} · ${d.servicio.nombre}", fontWeight = FontWeight.SemiBold)
                                Text("Dueño: ${d.mascota.dueno.nombre}", style = MaterialTheme.typography.bodySmall)
                                EstadoCitaChip(d.cita.estado)
                            }
                            when (d.cita.estado) {
                                EstadoCita.PENDIENTE.name -> TextButton(onClick = { vm.confirmar(d) }) { Text("Confirmar") }
                                EstadoCita.CONFIRMADA.name -> TextButton(onClick = { onAtender(d.cita.id) }) { Text("Atender") }
                            }
                        }
                    }
                }
            }
        }
    }
}
