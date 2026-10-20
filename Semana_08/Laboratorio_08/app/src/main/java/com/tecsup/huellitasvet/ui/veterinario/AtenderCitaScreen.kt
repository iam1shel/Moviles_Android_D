package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel

@Composable
fun AtenderCitaScreen(vm: AtenderCitaViewModel, onBack: () -> Unit) {
    val detalle by vm.cita.collectAsState()
    val errorDiag by vm.errorDiagnostico.collectAsState()
    val errorPeso by vm.errorPeso.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val listo by vm.listo.collectAsState()
    var peso by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }
    var tratamiento by remember { mutableStateOf("") }
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(detalle) {
        if (peso.isEmpty()) peso = detalle?.mascota?.mascota?.peso?.toString().orEmpty()
    }
    LaunchedEffect(listo) { if (listo) onBack() }
    LaunchedEffect(mensaje) { mensaje?.let { snack.showSnackbar(it) } }

    val d = detalle
    Scaffold(
        topBar = { AppTopBar("Atender cita", onBack) },
        snackbarHost = { SnackbarHost(snack) }
    ) { padding ->
        if (d == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        val m = d.mascota.mascota
        val especieTxt = when (m.especie) {
            Especie.PERRO.name -> "Perro"
            Especie.GATO.name -> "Gato"
            else -> "Otro"
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MascotaAvatar(m.especie, tamano = 64.dp, nombre = m.nombre)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("${m.nombre} · $especieTxt ${m.raza}", fontWeight = FontWeight.Bold)
                    Text("${m.peso} kg · ${d.mascota.dueno.nombre}", style = MaterialTheme.typography.bodySmall)
                    Text("${d.servicio.nombre} · ${d.cita.fecha} · ${d.cita.hora}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Registro clínico", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            VetTextField(
                peso, { peso = it }, "Peso (kg)",
                error = errorPeso,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Text("Peso anterior ${m.peso} kg", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            VetTextField(diagnostico, { diagnostico = it }, "Diagnóstico", error = errorDiag, singleLine = false, minLines = 3)
            Spacer(Modifier.height(8.dp))
            VetTextField(tratamiento, { tratamiento = it }, "Tratamiento", singleLine = false, minLines = 3)
            Spacer(Modifier.height(8.dp))
            Text("Se actualizará el peso de ${m.nombre} a ${peso.ifBlank { m.peso }} kg", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(16.dp))
            VetButton("Registrar atención", onClick = {
                vm.registrar(diagnostico, tratamiento, peso.replace(",", ".").toDoubleOrNull() ?: 0.0)
            })
            Spacer(Modifier.height(8.dp))
            VetButton("Cancelar cita", onClick = { vm.cancelar() }, outlined = true, containerColor = MaterialTheme.colorScheme.error)
        }
    }
}
