package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.DatoGrafico
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.graficos.GraficoBarras
import com.tecsup.huellitasvet.ui.components.graficos.GraficoDona
import com.tecsup.huellitasvet.ui.theme.AtendidaColor
import com.tecsup.huellitasvet.ui.theme.AzulVet
import com.tecsup.huellitasvet.ui.theme.CanceladaColor
import com.tecsup.huellitasvet.ui.theme.ConfirmadaColor
import com.tecsup.huellitasvet.ui.theme.PendienteColor
import com.tecsup.huellitasvet.ui.theme.TextoPrincipal
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PanelScreen(vm: PanelViewModel, onVerAgenda: () -> Unit) {
    val hoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val conteoEstados by vm.porEstado.collectAsState()
    val conteoServicios by vm.porServicio.collectAsState()
    val porEstado = conteoEstados.map { (estado, cantidad) ->
        DatoGrafico(estado.etiqueta(), cantidad.toFloat(), estado.color())
    }
    val coloresServicio = listOf(AzulVet, PendienteColor, ConfirmadaColor, AtendidaColor)
    val porServicio = conteoServicios.mapIndexed { i, item ->
        DatoGrafico(item.nombre, item.cantidad.toFloat(), coloresServicio[i % coloresServicio.size])
    }
    val fechaTxt = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE d MMM", Locale("es")))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Panel de la clínica", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(fechaTxt.replaceFirstChar { it.uppercase() }, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = {}) { Icon(Icons.Outlined.Notifications, contentDescription = null) }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MiniContador(hoy.toString(), "Citas hoy", ConfirmadaColor, Modifier.weight(1f))
            MiniContador(pendientes.toString(), "Pendientes", PendienteColor, Modifier.weight(1f))
            MiniContador(atendidas.toString(), "Atendidas", AtendidaColor, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Ingresos del mes", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodySmall)
                Text("S/ ${"%,.2f".format(ingresos)}", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("$atendidas atenciones", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Citas por estado", fontWeight = FontWeight.SemiBold)
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.fillMaxWidth()) {
            GraficoDona(porEstado)
        }
        Spacer(Modifier.height(16.dp))
        Text("Citas por servicio", fontWeight = FontWeight.SemiBold)
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), modifier = Modifier.fillMaxWidth()) {
            GraficoBarras(porServicio)
        }
        if (pendientes > 0) {
            Spacer(Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED), contentColor = TextoPrincipal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("$pendientes citas por confirmar para hoy y mañana", modifier = Modifier.weight(1f))
                    TextButton(onClick = onVerAgenda) { Text("Ver") }
                }
            }
        }
    }
}

private fun EstadoCita.etiqueta(): String = when (this) {
    EstadoCita.PENDIENTE -> "Pendiente"
    EstadoCita.CONFIRMADA -> "Confirmada"
    EstadoCita.ATENDIDA -> "Atendida"
    EstadoCita.CANCELADA -> "Cancelada"
}

private fun EstadoCita.color(): Color = when (this) {
    EstadoCita.PENDIENTE -> PendienteColor
    EstadoCita.CONFIRMADA -> ConfirmadaColor
    EstadoCita.ATENDIDA -> AtendidaColor
    EstadoCita.CANCELADA -> CanceladaColor
}

@Composable
private fun MiniContador(valor: String, etiqueta: String, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(etiqueta, color = color, style = MaterialTheme.typography.bodySmall)
        }
    }
}
