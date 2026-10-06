package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.theme.AzulClaro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEspecialidades: () -> Unit,
    onMisCitas: () -> Unit,
    onResultados: () -> Unit,
    onPerfil: () -> Unit,
    onNotificaciones: () -> Unit
) {
    var tab by remember { mutableIntStateOf(0) }
    val nombre = Repositorio.usuarioActual?.nombre ?: "Paciente"
    val destacadas = Repositorio.especialidadesDestacadas()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hola, $nombre", fontWeight = FontWeight.Bold)
                        Text(
                            text = "¿Qué necesitas hoy?",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNotificaciones) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1; onMisCitas() },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                    label = { Text("Citas") }
                )
                NavigationBarItem(
                    selected = tab == 2,
                    onClick = { tab = 2; onResultados() },
                    icon = { Icon(Icons.Default.Science, contentDescription = null) },
                    label = { Text("Resultados") }
                )
                NavigationBarItem(
                    selected = tab == 3,
                    onClick = { tab = 3; onPerfil() },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                onClick = onEspecialidades,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Agendar cita",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Elige especialidad, médico y horario",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Especialidades destacadas", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(destacadas, key = { it.id }) { esp ->
                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .clickable {
                                onEspecialidades()
                            },
                        colors = CardDefaults.cardColors(containerColor = AzulClaro)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(esp.nombre, fontWeight = FontWeight.SemiBold)
                            Text(
                                esp.descripcion,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Card(
                onClick = onMisCitas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Mis citas", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Revisa o cancela tus reservas",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
