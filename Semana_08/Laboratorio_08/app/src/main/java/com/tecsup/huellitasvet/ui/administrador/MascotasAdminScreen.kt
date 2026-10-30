package com.tecsup.huellitasvet.ui.administrador

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.AdministracionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MascotasAdminScreen(
    vm: AdministracionViewModel,
    onMascota: (Int) -> Unit,
    onAgregar: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val anio = LocalDate.now().year
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Outlined.Add, contentDescription = "Registrar mascota", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Administración de mascotas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    "Registra una mascota y elige su raza.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
            }
            if (mascotas.isEmpty()) {
                item {
                    Text(
                        "Todavía no hay mascotas registradas.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
            items(mascotas, key = { it.mascota.id }) { item ->
                val m = item.mascota
                val edad = (anio - m.anioNacimiento).coerceAtLeast(0)
                val nacimiento = m.fechaNacimiento.takeIf { it.isNotBlank() }?.let { iso ->
                    runCatching { LocalDate.parse(iso).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }.getOrNull()
                }
                val especieTxt = when (m.especie) {
                    Especie.PERRO.name -> "Perro"
                    Especie.GATO.name -> "Gato"
                    else -> "Otro"
                }
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onMascota(m.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(m.especie, nombre = m.nombre)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(m.nombre, fontWeight = FontWeight.SemiBold)
                            Text("$especieTxt · ${m.raza}", style = MaterialTheme.typography.bodySmall)
                            Text(
                                listOfNotNull(
                                    "Dueño: ${item.dueno.nombreCompleto()}",
                                    nacimiento?.let { "nació el $it" },
                                    "$edad años"
                                ).joinToString(" · "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
