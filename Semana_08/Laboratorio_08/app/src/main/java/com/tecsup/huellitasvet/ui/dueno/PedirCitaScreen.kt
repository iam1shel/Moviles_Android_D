package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    onBack: () -> Unit,
    onCreada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val aceptar by vm.aceptarCitas.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val creada by vm.creada.collectAsState()
    val snack = remember { SnackbarHostState() }
    val fechas = (0L..7L).map { LocalDate.now().plusDays(it) }

    LaunchedEffect(mascotas, mascotaId) {
        if (mascotaId == 0 && mascotas.isNotEmpty()) vm.mascotaId.value = mascotas.first().id
    }
    LaunchedEffect(mensaje) { mensaje?.let { snack.showSnackbar(it); vm.limpiarMensaje() } }
    LaunchedEffect(creada) { if (creada) onCreada() }

    val mascota = mascotas.find { it.id == mascotaId }
    val servicio = servicios.find { it.id == servicioId }
    val listo = mascotaId != 0 && servicioId != 0 && fecha.isNotBlank() && hora.isNotBlank() && aceptar

    Scaffold(
        topBar = { AppTopBar("Pedir cita", onBack) },
        snackbarHost = { SnackbarHost(snack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (!aceptar) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("La clínica no está recibiendo citas", modifier = Modifier.padding(16.dp))
                }
                Spacer(Modifier.height(12.dp))
            }
            Text("¿Quién viene?", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mascotas.forEach { m ->
                    FilterChip(
                        selected = mascotaId == m.id,
                        onClick = { vm.mascotaId.value = m.id },
                        label = { Text(m.nombre) },
                        leadingIcon = { MascotaAvatar(m.especie, tamano = 28.dp, nombre = m.nombre) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Servicio", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            servicios.forEach { s ->
                Card(
                    onClick = { vm.servicioId.value = s.id },
                    colors = CardDefaults.cardColors(
                        containerColor = if (servicioId == s.id) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(s.icono)
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(s.nombre, fontWeight = FontWeight.SemiBold)
                            Text("${s.duracionMin} min", style = MaterialTheme.typography.bodySmall)
                        }
                        Text("S/ ${s.precio.toInt()}")
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Fecha", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
            ) {
                fechas.forEach { d ->
                    FechaChip(d, seleccionado = fecha == d.toString(), onClick = { vm.seleccionarFecha(d.toString()) })
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Hora", fontWeight = FontWeight.SemiBold)
            FlowRow {
                Horarios.todos.forEach { h ->
                    HoraChip(
                        hora = h,
                        seleccionado = hora == h,
                        ocupado = h in ocupadas,
                        deshabilitado = vm.horaPasada(h),
                        onClick = { vm.hora.value = h }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            if (mascota != null && servicio != null && hora.isNotEmpty()) {
                val fechaTxt = runCatching {
                    LocalDate.parse(fecha).format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es")))
                }.getOrDefault(fecha)
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${mascota.nombre} · ${servicio.nombre}", fontWeight = FontWeight.SemiBold)
                        Text("$fechaTxt · $hora · S/ ${servicio.precio}")
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            VetButton("Confirmar cita", onClick = { vm.confirmar() }, enabled = listo)
        }
    }
}
