package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.ChipPildora
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PacientesScreen(vm: PacientesViewModel, onFicha: (Int) -> Unit) {
    val lista by vm.pacientes.collectAsState()
    val consulta by vm.consulta.collectAsState()
    val filtro by vm.especie.collectAsState()
    val ultimasVisitas by vm.ultimasVisitas.collectAsState()
    val anio = LocalDate.now().year
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(Modifier.fillMaxSize()) {
        AppTopBar("Pacientes")
        OutlinedTextField(
            value = consulta,
            onValueChange = { vm.consulta.value = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Buscar mascota o dueño...") },
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            singleLine = true
        )
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChipPildora("Todos", filtro == null, { vm.especie.value = null })
            ChipPildora("Perros", filtro == Especie.PERRO, { vm.especie.value = Especie.PERRO })
            ChipPildora("Gatos", filtro == Especie.GATO, { vm.especie.value = Especie.GATO })
            ChipPildora("Otros", filtro == Especie.OTRO, { vm.especie.value = Especie.OTRO })
        }
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(lista, key = { it.mascota.id }) { item ->
                val m = item.mascota
                val edad = (anio - m.anioNacimiento).coerceAtLeast(0)
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onFicha(m.id) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(m.especie, nombre = m.nombre)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(m.nombre, fontWeight = FontWeight.SemiBold)
                            Text("${m.raza} · $edad años", style = MaterialTheme.typography.bodySmall)
                            Text("Dueño: ${item.dueno.nombre}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                ultimasVisitas[m.id]?.let { "Última visita: ${it.format(formato)}" }
                                    ?: "Sin visitas registradas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(">", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
