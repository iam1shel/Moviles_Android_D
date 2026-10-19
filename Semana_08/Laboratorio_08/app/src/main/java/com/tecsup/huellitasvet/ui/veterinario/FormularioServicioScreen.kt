package com.tecsup.huellitasvet.ui.veterinario

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.ChipPildora
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

@Composable
fun FormularioServicioScreen(
    vm: ServiciosViewModel,
    servicioId: Int,
    onBack: () -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val existente = servicios.find { it.id == servicioId }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf(30) }
    var precio by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("heart") }
    var activo by remember { mutableStateOf(true) }
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorPrecio by remember { mutableStateOf<String?>(null) }
    var cargado by remember { mutableStateOf(false) }

    LaunchedEffect(existente) {
        val s = existente
        if (servicioId != 0 && s != null && !cargado) {
            nombre = s.nombre
            descripcion = s.descripcion
            duracion = s.duracionMin
            precio = s.precio.toString()
            icono = s.icono
            activo = s.activo
            cargado = true
        }
    }

    Scaffold(topBar = { AppTopBar(if (servicioId == 0) "Nuevo servicio" else "Editar servicio", onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            VetTextField(nombre, { nombre = it }, "Nombre del servicio", error = errorNombre)
            Spacer(Modifier.height(8.dp))
            Text("Duración", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 45, 60).forEach { min ->
                    ChipPildora("$min min", duracion == min, { duracion = min })
                }
            }
            Spacer(Modifier.height(8.dp))
            VetTextField(
                precio, { precio = it }, "Precio (S/)",
                error = errorPrecio,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(Modifier.height(8.dp))
            VetTextField(descripcion, { descripcion = it }, "Descripción", singleLine = false, minLines = 3)
            Spacer(Modifier.height(8.dp))
            Text("Ícono", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf("heart", "syringe", "scissors").forEach { i ->
                    FilterChip(
                        selected = icono == i,
                        onClick = { icono = i },
                        label = { IconoServicio(i) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Servicio activo", modifier = Modifier.weight(1f))
                Switch(checked = activo, onCheckedChange = { activo = it })
            }
            Spacer(Modifier.height(24.dp))
            VetButton("Guardar servicio", onClick = {
                var ok = true
                if (nombre.isBlank()) { errorNombre = "El nombre es obligatorio"; ok = false } else errorNombre = null
                val p = precio.replace(",", ".").toDoubleOrNull() ?: 0.0
                if (p <= 0) { errorPrecio = "El precio debe ser mayor que 0"; ok = false } else errorPrecio = null
                if (!ok) return@VetButton
                vm.guardar(
                    Servicio(
                        id = servicioId,
                        nombre = nombre.trim(),
                        descripcion = descripcion.trim(),
                        duracionMin = duracion,
                        precio = p,
                        activo = activo,
                        icono = icono
                    )
                )
                onBack()
            })
            Spacer(Modifier.height(8.dp))
            VetButton("Cancelar", onClick = onBack, outlined = true)
            if (servicioId != 0 && existente != null) {
                Spacer(Modifier.height(8.dp))
                VetButton(
                    "Eliminar servicio",
                    onClick = {
                        vm.eliminar(existente)
                        onBack()
                    },
                    outlined = true,
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
