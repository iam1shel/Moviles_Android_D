package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CitaCard(
    detalle: CitaConDetalle,
    onCancelar: (() -> Unit)? = null,
    onReprogramar: (() -> Unit)? = null,
    onConfirmar: (() -> Unit)? = null,
    onAtender: (() -> Unit)? = null,
    mostrarDueno: Boolean = false
) {
    val cita = detalle.cita
    val fechaTxt = runCatching {
        LocalDate.parse(cita.fecha).format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es")))
    }.getOrDefault(cita.fecha)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconoServicio(detalle.servicio.icono)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(detalle.servicio.nombre, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${detalle.mascota.mascota.nombre}  ·  $fechaTxt · ${cita.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (mostrarDueno) {
                        Text(
                            "Dueño: ${detalle.mascota.dueno.nombre}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                EstadoCitaChip(cita.estado)
            }
            val acciones = listOfNotNull(
                onCancelar?.let { "Cancelar" to it },
                onReprogramar?.let { "Reprogramar" to it },
                onConfirmar?.let { "Confirmar" to it },
                onAtender?.let { "Atender" to it }
            )
            if (acciones.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    acciones.forEach { (texto, accion) ->
                        val esCancelar = texto == "Cancelar"
                        TextButton(onClick = accion) {
                            Text(
                                texto,
                                color = if (esCancelar) Color(0xFFDC2626)
                                else MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
