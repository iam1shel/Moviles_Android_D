package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel

@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onPedirCita: () -> Unit
) {
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var tab by remember { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<CitaConDetalle?>(null) }
    val porReprogramar by vm.porReprogramar.collectAsState()
    val snack = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) { mensaje?.let { snack.showSnackbar(it); vm.limpiarMensaje() } }

    Scaffold(
        topBar = { AppTopBar("Mis citas") },
        snackbarHost = { SnackbarHost(snack) },
        floatingActionButton = {
            FloatingActionButton(onClick = onPedirCita, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Outlined.Add, contentDescription = "Pedir cita", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Próximas") })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Historial") })
            }
            val lista = if (tab == 0) proximas else historial
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.cita.id }) { d ->
                    val puede = vm.puedeCancelar(d)
                    CitaCard(
                        detalle = d,
                        onCancelar = if (tab == 0 && puede) ({ porCancelar = d }) else null,
                        onReprogramar = if (tab == 0 && puede) ({ vm.abrirReprogramar(d) }) else null
                    )
                }
                if (tab == 0) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Puedes cancelar hasta 2 horas antes", modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
    porCancelar?.let { d ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("Cancelar cita") },
            text = { Text("¿Cancelar la cita de ${d.mascota.mascota.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(d.cita.id, d.cita.fecha, d.cita.hora, d.cita.estado)
                    porCancelar = null
                }) { Text("Sí, cancelar") }
            },
            dismissButton = { TextButton(onClick = { porCancelar = null }) { Text("No") } }
        )
    }
    if (porReprogramar != null) {
        DialogReprogramar(vm)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DialogReprogramar(vm: MisCitasViewModel) {
    val fecha by vm.fechaReprogramar.collectAsState()
    val hora by vm.horaReprogramar.collectAsState()
    val ocupadas by vm.horasOcupadasReprogramar.collectAsState()
    val error by vm.errorReprogramar.collectAsState()
    AlertDialog(
        onDismissRequest = { vm.cerrarReprogramar() },
        title = { Text("Reprogramar cita") },
        text = {
            Column {
                Text("Elige una nueva fecha y hora libre.")
                Spacer(Modifier.height(8.dp))
                FlowRow {
                    vm.fechasDisponibles().forEach { d ->
                        FechaChip(
                            d,
                            seleccionado = fecha == d.toString(),
                            onClick = { vm.seleccionarFechaReprogramar(d.toString()) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                FlowRow {
                    Horarios.todos.forEach { h ->
                        HoraChip(
                            hora = h,
                            seleccionado = hora == h,
                            ocupado = h in ocupadas,
                            deshabilitado = vm.horaPasada(h, fecha),
                            onClick = { vm.seleccionarHoraReprogramar(h) }
                        )
                    }
                }
                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { vm.reprogramar() }, enabled = hora.isNotBlank()) {
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = { vm.cerrarReprogramar() }) { Text("Cerrar") } }
    )
}
