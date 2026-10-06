package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio

@OptIn(ExperimentalMaterial3Api::class)
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
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar cita") },
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
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Especialidad: ${especialidad?.nombre}")
                    Text("Médico: ${medico?.nombre}")
                    Text("Fecha: $fecha")
                    Text("Hora: $hora")
                    Text("Paciente: ${usuario?.nombre ?: "—"}")
                }
            }
            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    val userId = usuario?.id
                    if (userId == null) {
                        error = "Debes iniciar sesión"
                        return@Button
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
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Confirmar cita") }
        }
    }
}
