package com.tecsup.huellitasvet.ui.veterinario

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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

@Composable
fun ServiciosScreen(vm: ServiciosViewModel, onEditar: (Int) -> Unit) {
    val servicios by vm.servicios.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snack = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) { mensaje?.let { snack.showSnackbar(it); vm.limpiarMensaje() } }

    Scaffold(
        topBar = { AppTopBar("Servicios") },
        snackbarHost = { SnackbarHost(snack) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEditar(0) }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Outlined.Add, contentDescription = "Nuevo", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(servicios, key = { it.id }) { s ->
                ServicioItem(s, onToggle = { vm.alternar(s) }, onEditar = { onEditar(s.id) })
            }
        }
    }
}

@Composable
fun ServicioItem(s: Servicio, onToggle: () -> Unit, onEditar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconoServicio(s.icono)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(s.nombre, fontWeight = FontWeight.SemiBold)
                Text("${s.duracionMin} min · S/ ${s.precio}", style = MaterialTheme.typography.bodySmall)
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
            IconButton(onClick = onEditar) { Icon(Icons.Outlined.Edit, contentDescription = "Editar") }
        }
    }
}
