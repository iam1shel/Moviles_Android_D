package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.FilaMedicoLista
import com.saludplus.citas.ui.components.SaludPlusTopBar
import com.saludplus.citas.ui.theme.AzulGris
import com.saludplus.citas.ui.theme.GrisFondo

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onVolver: () -> Unit,
    onSeleccionar: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var mostrarBusqueda by remember { mutableStateOf(false) }
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    val lista = Repositorio.buscarMedicos(especialidadId, query)
    val titulo = especialidad?.let { "Médicos de ${it.nombre}" } ?: "Médicos"

    Scaffold(
        containerColor = GrisFondo,
        topBar = {
            SaludPlusTopBar(
                title = titulo,
                onVolver = onVolver,
                actions = {
                    IconButton(onClick = { mostrarBusqueda = !mostrarBusqueda }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = AzulGris)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (mostrarBusqueda) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Buscar médico...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.id }) { medico ->
                    FilaMedicoLista(
                        medico = medico,
                        especialidad = especialidad?.nombre ?: "",
                        onClick = { onSeleccionar(medico.id) },
                        modifier = Modifier.clickable { onSeleccionar(medico.id) }
                    )
                }
            }
        }
    }
}
