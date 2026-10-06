package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun DetalleCitaScreen(
    citaId: Int,
    onVolver: () -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val esp = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de cita") },
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
            if (cita == null) {
                Text("La cita ya no existe")
                Spacer(Modifier.height(12.dp))
                Button(onClick = onVolver) { Text("Volver") }
            } else {
                Text("Cita #${cita.id}", fontWeight = FontWeight.Bold)
                Text("${esp?.nombre}")
                Text("${medico?.nombre}")
                Text("${cita.fecha} · ${cita.hora}")
                Text("Estado: ${cita.estado}")
                Spacer(Modifier.weight(1f))
                OutlinedButton(
                    onClick = { mostrarDialogo = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Cancelar cita") }
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("¿Cancelar cita?") },
            text = { Text("Se eliminará de tu lista de citas.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        Repositorio.cancelarCita(citaId)
                        mostrarDialogo = false
                        onVolver()
                    }
                ) { Text("Sí, cancelar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) { Text("No") }
            }
        )
    }
}
