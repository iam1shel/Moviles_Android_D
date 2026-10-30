package com.tecsup.huellitasvet.ui.administrador

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Razas
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.ChipPildora
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.SelectorLista
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

@Composable
fun FormularioMascotaAdminScreen(
    vm: MascotaViewModel,
    duenos: List<Usuario>,
    mascotaId: Int,
    onBack: () -> Unit
) {
    val existente by vm.mascota.collectAsState()
    val errorNombre by vm.errorNombre.collectAsState()
    val errorEspecie by vm.errorEspecie.collectAsState()
    val errorSexo by vm.errorSexo.collectAsState()
    val errorAnio by vm.errorAnio.collectAsState()
    val errorPeso by vm.errorPeso.collectAsState()
    val errorRaza by vm.errorRaza.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val guardado by vm.guardado.collectAsState()
    val snack = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf(Especie.PERRO.name) }
    var sexo by remember { mutableStateOf(Sexo.MACHO.name) }
    var duenoId by remember { mutableIntStateOf(0) }
    var errorDueno by remember { mutableStateOf<String?>(null) }
    var cargado by remember { mutableStateOf(false) }
    var confirmarBorrar by remember { mutableStateOf(false) }

    val duenoTxt = duenos.find { it.id == duenoId }?.nombreCompleto().orEmpty()
    val opcionesRaza = Razas.de(especie).let { lista ->
        if (raza.isNotBlank() && raza !in lista) listOf(raza) + lista else lista
    }

    LaunchedEffect(existente) {
        val m = existente
        if (mascotaId != 0 && m != null && !cargado) {
            nombre = m.nombre
            raza = m.raza
            anio = m.anioNacimiento.toString()
            peso = m.peso.toString()
            especie = m.especie
            sexo = m.sexo
            duenoId = m.duenoId
            cargado = true
        }
    }
    LaunchedEffect(guardado) { if (guardado) { vm.consumirGuardado(); onBack() } }
    LaunchedEffect(mensaje) { mensaje?.let { snack.showSnackbar(it); vm.limpiarMensaje() } }

    Scaffold(
        topBar = { AppTopBar(if (mascotaId == 0) "Registrar mascota" else "Editar mascota", onBack) },
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
            SelectorLista(
                etiqueta = "Dueño",
                valor = duenoTxt,
                opciones = duenos.map { it.nombreCompleto() },
                onElegir = { elegido ->
                    duenoId = duenos.firstOrNull { it.nombreCompleto() == elegido }?.id ?: 0
                    errorDueno = null
                },
                error = errorDueno,
                placeholder = "Elige el dueño"
            )
            Spacer(Modifier.height(8.dp))
            VetTextField(nombre, { nombre = it }, "Nombre", error = errorNombre)
            Spacer(Modifier.height(12.dp))
            Text("Especie", style = MaterialTheme.typography.titleSmall, modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.align(Alignment.Start)) {
                listOf(Especie.PERRO to "Perro", Especie.GATO to "Gato", Especie.OTRO to "Otro").forEach { (e, t) ->
                    ChipPildora(t, especie == e.name, {
                        especie = e.name
                        if (raza !in Razas.de(e.name)) raza = ""
                    })
                }
            }
            if (errorEspecie != null) {
                Text(errorEspecie!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            SelectorLista(
                etiqueta = "Raza",
                valor = raza,
                opciones = opcionesRaza,
                onElegir = { raza = it },
                error = errorRaza,
                placeholder = "Elige la raza"
            )
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
            if (errorSexo != null) {
                Text(errorSexo!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(24.dp))
            VetButton("Guardar mascota", onClick = {
                if (duenoId == 0) {
                    errorDueno = "Elige el dueño de la mascota"
                    return@VetButton
                }
                errorDueno = null
                vm.guardar(
                    Mascota(
                        id = mascotaId,
                        duenoId = duenoId,
                        nombre = nombre,
                        especie = especie,
                        raza = raza,
                        anioNacimiento = anio.toIntOrNull() ?: 0,
                        peso = peso.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        sexo = sexo
                    ),
                    exigirRazaDeCatalogo = true
                )
            })
            Spacer(Modifier.height(8.dp))
            VetButton("Cancelar", onClick = onBack, outlined = true)
            if (mascotaId != 0 && existente != null) {
                Spacer(Modifier.height(8.dp))
                VetButton(
                    "Eliminar mascota",
                    onClick = { confirmarBorrar = true },
                    outlined = true,
                    containerColor = Color(0xFFDC2626)
                )
            }
        }
    }

    val mascota = existente
    if (confirmarBorrar && mascota != null) {
        AlertDialog(
            onDismissRequest = { confirmarBorrar = false },
            title = { Text("Eliminar mascota") },
            text = { Text("¿Eliminar a ${mascota.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarBorrar = false
                    vm.eliminar(mascota, onBack)
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { confirmarBorrar = false }) { Text("Cancelar") } }
        )
    }
}
