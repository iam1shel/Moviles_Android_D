package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.FilaDetalleCita
import com.saludplus.citas.ui.components.SaludPlusPrimaryButton
import com.saludplus.citas.ui.components.SaludPlusTopBar
import com.saludplus.citas.ui.components.TarjetaMedicoResumen
import com.saludplus.citas.ui.theme.AzulTexto
import com.saludplus.citas.ui.theme.GrisBorde

@Composable
fun ConfirmarCitaScreen(
    especialidadId: Int,
    medicoId: Int,
    fecha: String,
    hora: String,
    onVolver: () -> Unit,
    onConfirmado: (citaId: Int) -> Unit
) {
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val medico = Repositorio.obtenerMedico(medicoId)
    val usuario = Repositorio.usuarioActual
    var motivo by remember { mutableStateOf("Consulta de rutina") }
    var error by remember { mutableStateOf<String?>(null) }
    val sede = Repositorio.sedeSeleccionada ?: "La Molina"
    val finHora = remember(hora) {
        runCatching {
            val partes = hora.split(":")
            val h = partes[0].toInt()
            val m = partes[1].toInt() + 30
            val nh = h + if (m >= 60) 1 else 0
            val nm = m % 60
            "%02d:%02d".format(nh, nm)
        }.getOrDefault(hora)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            SaludPlusTopBar(title = "Confirmar cita", onVolver = onVolver)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                if (medico != null) {
                    TarjetaMedicoResumen(
                        medico = medico,
                        especialidad = especialidad?.nombre ?: ""
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                FilaDetalleCita(
                    icon = Icons.Default.CalendarMonth,
                    label = "Fecha",
                    value = CalendarioUtils.fechaEnEspanol(fecha)
                )
                HorizontalDivider(color = GrisBorde)
                FilaDetalleCita(
                    icon = Icons.Default.AccessTime,
                    label = "Hora",
                    value = "$hora a $finHora"
                )
                HorizontalDivider(color = GrisBorde)
                FilaDetalleCita(
                    icon = Icons.Default.MedicalServices,
                    label = "Tipo de atención",
                    value = "Consulta presencial"
                )
                HorizontalDivider(color = GrisBorde)
                FilaDetalleCita(
                    icon = Icons.Default.LocationOn,
                    label = "Dirección",
                    value = "Sede $sede, Lima"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Motivo de consulta (opcional)",
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GrisBorde,
                        unfocusedBorderColor = GrisBorde
                    )
                )
                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = Color(0xFFC62828))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            SaludPlusPrimaryButton(
                text = "Agendar cita",
                onClick = {
                    val userId = usuario?.id
                    if (userId == null) {
                        error = "Debes iniciar sesión"
                        return@SaludPlusPrimaryButton
                    }
                    val cita = Repositorio.agendarCita(
                        usuarioId = userId,
                        medicoId = medicoId,
                        especialidadId = especialidadId,
                        fecha = fecha,
                        hora = hora
                    )
                    if (cita == null) {
                        error = "Ese horario ya no está disponible"
                    } else {
                        onConfirmado(cita.id)
                    }
                }
            )
        }
    }
}
