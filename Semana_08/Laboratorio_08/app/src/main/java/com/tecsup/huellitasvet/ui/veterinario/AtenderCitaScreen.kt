package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FormaCampo = RoundedCornerShape(12.dp)

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
        topBar = { CabeceraAtender(onBack) },
        snackbarHost = { SnackbarHost(snack) },
        contentWindowInsets = WindowInsets(0.dp)
    ) { padding ->
        if (d == null) {
            Text("Cargando...", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }
        val m = d.mascota.mascota
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            ResumenCitaCard(d)
            Spacer(Modifier.height(14.dp))
            Text(
                "Registro clínico",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                CampoClinico(
                    valor = peso,
                    onValor = { peso = it },
                    etiqueta = "Peso (kg)",
                    modifier = Modifier.weight(1f),
                    error = errorPeso,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                PesoAnteriorCard(
                    pesoAnterior = m.peso,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp)
                        .height(56.dp)
                )
            }
            Spacer(Modifier.height(6.dp))
            CampoClinico(
                valor = diagnostico,
                onValor = { diagnostico = it },
                etiqueta = "Diagnóstico",
                modifier = Modifier.fillMaxWidth(),
                error = errorDiag,
                multilinea = true
            )
            Spacer(Modifier.height(6.dp))
            CampoClinico(
                valor = tratamiento,
                onValor = { tratamiento = it },
                etiqueta = "Tratamiento",
                modifier = Modifier.fillMaxWidth(),
                multilinea = true
            )
            Spacer(Modifier.height(12.dp))
            AvisoPeso("Se actualizará el peso de ${m.nombre} a ${peso.ifBlank { m.peso }} kg")
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    vm.registrar(diagnostico, tratamiento, peso.replace(",", ".").toDoubleOrNull() ?: 0.0)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = FormaCampo,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) { Text("Registrar atención", fontWeight = FontWeight.SemiBold) }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { vm.cancelar() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = FormaCampo,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) { Text("Cancelar cita", fontWeight = FontWeight.SemiBold) }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CabeceraAtender(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            "Atender cita",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ResumenCitaCard(d: CitaConDetalle) {
    val m = d.mascota.mascota
    val esHembra = m.sexo == Sexo.HEMBRA.name
    val especieTxt = when (m.especie) {
        Especie.PERRO.name -> if (esHembra) "Perra" else "Perro"
        Especie.GATO.name -> if (esHembra) "Gata" else "Gato"
        else -> "Otro"
    }
    val edad = (LocalDate.now().year - m.anioNacimiento).coerceAtLeast(0)
    val edadTxt = if (edad == 1) "1 año" else "$edad años"
    val sexoTxt = if (esHembra) "Hembra" else "Macho"
    val fechaTxt = formatearFechaCorta(d.cita.fecha)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = FormaCampo,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MascotaAvatar(m.especie, tamano = 48.dp, nombre = m.nombre)
            Spacer(Modifier.width(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "${m.nombre} · $especieTxt ${m.raza.lowercase()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "$edadTxt · $sexoTxt · Dueño: ${nombreCorto(d.mascota.dueno.nombre)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconoServicio(d.servicio.icono, tamano = 14.dp)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${d.servicio.nombre} · $fechaTxt · ${d.cita.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun PesoAnteriorCard(pesoAnterior: Double, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = FormaCampo,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Peso anterior",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                )
            }
            Text(
                "$pesoAnterior kg",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun CampoClinico(
    valor: String,
    onValor: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    multilinea: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValor,
        label = { Text(etiqueta) },
        modifier = if (multilinea) modifier.heightIn(min = 80.dp) else modifier,
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        keyboardOptions = keyboardOptions,
        singleLine = !multilinea,
        minLines = if (multilinea) 2 else 1,
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = FormaCampo,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun AvisoPeso(texto: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                texto,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

private fun formatearFechaCorta(fecha: String): String =
    runCatching {
        val dia = LocalDate.parse(fecha)
        val nombreDia = dia.format(DateTimeFormatter.ofPattern("EEE", Locale("es")))
            .trimEnd('.')
            .replaceFirstChar { it.titlecase(Locale("es")) }
        "$nombreDia ${dia.dayOfMonth}"
    }.getOrDefault(fecha)

private fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(Regex("\\s+"))
    return if (partes.size >= 2) "${partes[0]} ${partes[1].first()}." else nombre
}
