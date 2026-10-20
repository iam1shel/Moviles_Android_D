package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.ChipPildora
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    duenoId: Int,
    mascotaId: Int,
    onBack: () -> Unit
) {
    val existente by vm.mascota.collectAsState()
    val errorNombre by vm.errorNombre.collectAsState()
    val errorEspecie by vm.errorEspecie.collectAsState()
    val errorSexo by vm.errorSexo.collectAsState()
    val errorAnio by vm.errorAnio.collectAsState()
    val errorPeso by vm.errorPeso.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val guardado by vm.guardado.collectAsState()
    val snack = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf(Especie.PERRO.name) }
    var sexo by remember { mutableStateOf(Sexo.MACHO.name) }
    var cargado by remember { mutableStateOf(false) }

    LaunchedEffect(existente) {
        val m = existente
        if (mascotaId != 0 && m != null && !cargado) {
            nombre = m.nombre
            raza = m.raza
            anio = m.anioNacimiento.toString()
            peso = m.peso.toString()
            especie = m.especie
            sexo = m.sexo
            cargado = true
        }
    }
    LaunchedEffect(guardado) { if (guardado) { vm.consumirGuardado(); onBack() } }
    LaunchedEffect(mensaje) { mensaje?.let { snack.showSnackbar(it); vm.limpiarMensaje() } }

    Scaffold(
        topBar = { AppTopBar(if (mascotaId == 0) "Nueva mascota" else "Editar mascota", onBack) },
        snackbarHost = { SnackbarHost(snack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MascotaAvatar(especie, tamano = 96.dp, nombre = nombre, mostrarPlus = true)
            Spacer(Modifier.height(20.dp))
            VetTextField(nombre, { nombre = it }, "Nombre", error = errorNombre)
            Spacer(Modifier.height(12.dp))
            Text("Especie", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.align(Alignment.Start)) {
                listOf(Especie.PERRO to "Perro", Especie.GATO to "Gato", Especie.OTRO to "Otro").forEach { (e, t) ->
                    ChipPildora(t, especie == e.name, { especie = e.name })
                }
            }
            if (errorEspecie != null) Text(errorEspecie!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            VetTextField(raza, { raza = it }, "Raza")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VetTextField(
                    anio, { anio = it }, "Año de nacimiento",
                    modifier = Modifier.weight(1f),
                    error = errorAnio,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                VetTextField(
                    peso, { peso = it }, "Peso (kg)",
                    modifier = Modifier.weight(1f),
                    error = errorPeso,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("Sexo", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.align(Alignment.Start)) {
                ChipPildora("Macho", sexo == Sexo.MACHO.name, { sexo = Sexo.MACHO.name })
                ChipPildora("Hembra", sexo == Sexo.HEMBRA.name, { sexo = Sexo.HEMBRA.name })
            }
            if (errorSexo != null) Text(errorSexo!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(24.dp))
            VetButton("Guardar mascota", onClick = {
                vm.guardar(
                    Mascota(
                        id = mascotaId,
                        duenoId = existente?.duenoId ?: duenoId,
                        nombre = nombre,
                        especie = especie,
                        raza = raza,
                        anioNacimiento = anio.toIntOrNull() ?: 0,
                        peso = peso.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        sexo = sexo
                    )
                )
            })
            Spacer(Modifier.height(8.dp))
            VetButton("Cancelar", onClick = onBack, outlined = true)
        }
    }
}
