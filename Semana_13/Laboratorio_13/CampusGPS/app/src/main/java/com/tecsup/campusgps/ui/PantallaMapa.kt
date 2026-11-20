package com.tecsup.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val context = LocalContext.current
    val estado by vm.estado.collectAsStateWithLifecycle()
    val lugares by vm.lugares.collectAsStateWithLifecycle()
    val seleccionadoId by vm.seleccionado.collectAsStateWithLifecycle()
    val masCercano by vm.masCercano.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("CampusGPS") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val fine = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    val coarse = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (fine || coarse) {
                        vm.buscar()
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                enabled = estado !is UbicacionUi.Buscando
            ) {
                Text("Obtener mi ubicación")
            }

            when (val e = estado) {
                UbicacionUi.Inicial -> {
                    Text("CampusGPS · proyecto base. Presiona el botón para buscar tu ubicación.")
                }
                UbicacionUi.Buscando -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text("Buscando...")
                    }
                }
                is UbicacionUi.Lista -> {
                    if (masCercano != null) {
                        val lugarCercanoItem = lugares.find { it.lugar.id == masCercano?.id }
                        val distanciaStr = lugarCercanoItem?.metros?.let { formatearDistancia(it) } ?: ""
                        Text(
                            text = "Más cercano: ${masCercano?.nombre} ($distanciaStr)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text("Latitud: ${e.posicion.latitud}, Longitud: ${e.posicion.longitud}")
                    }

                    OutlinedButton(
                        onClick = {
                            masCercano?.let { vm.seleccionar(it.id) }
                        },
                        enabled = masCercano != null
                    ) {
                        Text("Ir al más cercano")
                    }
                }
                is UbicacionUi.Error -> {
                    MensajeError(
                        errorState = e,
                        onReintentar = { vm.buscar() }
                    )
                }
            }

            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionadoId = seleccionadoId,
                masCercano = masCercano,
                onLugarClick = { id -> vm.seleccionar(id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            ListaLugares(
                lugares = lugares,
                seleccionadoId = seleccionadoId,
                onLugarClick = { id -> vm.seleccionar(id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
