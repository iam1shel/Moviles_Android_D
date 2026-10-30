package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onBack: (() -> Unit)? = null,
    onAtender: (Int) -> Unit
) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val hoy = LocalDate.now()
    val dias = (0L..6L).map { hoy.plusDays(it) }

    Column(Modifier.fillMaxSize()) {
        AgendaCabecera(onBack)
        SelectorFechas(
            dias = dias,
            hoy = hoy,
            seleccionada = fecha,
            onSeleccionar = { vm.fechaSeleccionada.value = it.toString() }
        )
        if (citas.isEmpty()) {
            AgendaVacia()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp)
            ) {
                items(citas, key = { it.cita.id }) { d ->
                    AgendaCitaItem(
                        detalle = d,
                        onConfirmar = { vm.confirmar(d) },
                        onAtender = { onAtender(d.cita.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AgendaCabecera(onBack: (() -> Unit)?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 4.dp)
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text(
            "Agenda",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SelectorFechas(
    dias: List<LocalDate>,
    hoy: LocalDate,
    seleccionada: String,
    onSeleccionar: (LocalDate) -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val espacio = 8.dp
        // ~4 chips visibles; la fracción extra deja asomar el siguiente para sugerir el scroll.
        val anchoChip = ((maxWidth - 32.dp - espacio * 3) / 4.3f).coerceIn(72.dp, 110.dp)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(espacio)
        ) {
            items(dias, key = { it.toString() }) { d ->
                FechaPildora(
                    fecha = d,
                    esHoy = d == hoy,
                    seleccionado = seleccionada == d.toString(),
                    ancho = anchoChip,
                    onClick = { onSeleccionar(d) }
                )
            }
        }
    }
}

@Composable
private fun FechaPildora(
    fecha: LocalDate,
    esHoy: Boolean,
    seleccionado: Boolean,
    ancho: Dp,
    onClick: () -> Unit
) {
    val etiqueta = if (esHoy) "Hoy" else fecha.dayOfWeek
        .getDisplayName(TextStyle.SHORT, Locale("es"))
        .removeSuffix(".")
        .replaceFirstChar { it.uppercase() }
    val colors = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        modifier = Modifier
            .width(ancho)
            .height(36.dp),
        shape = RoundedCornerShape(50),
        color = if (seleccionado) colors.primary else colors.surface,
        contentColor = if (seleccionado) colors.onPrimary else colors.onSurface,
        border = if (seleccionado) null else BorderStroke(1.dp, colors.outline.copy(alpha = 0.25f))
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(etiqueta, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.width(4.dp))
            Text("${fecha.dayOfMonth}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AgendaCitaItem(
    detalle: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val mascota = detalle.mascota.mascota
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            detalle.cita.hora,
            modifier = Modifier
                .width(48.dp)
                .padding(top = 14.dp),
            color = colors.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        // La línea ocupa toda la fila (incluido el margen inferior) para que se vea continua entre citas.
        Box(
            Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(colors.primary.copy(alpha = 0.15f))
        )
        Spacer(Modifier.width(12.dp))
        Surface(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(14.dp),
            color = colors.surface,
            contentColor = colors.onSurface,
            border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.2f))
        ) {
            Row(Modifier.padding(12.dp)) {
                MascotaAvatar(mascota.especie, tamano = 40.dp, nombre = mascota.nombre)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(mascota.nombre) }
                            append(" · ${detalle.servicio.nombre}")
                        },
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "Dueño: ${nombreCorto(detalle.mascota.dueno.nombre)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EstadoCitaChip(detalle.cita.estado)
                        Spacer(Modifier.weight(1f))
                        when (detalle.cita.estado) {
                            EstadoCita.PENDIENTE.name -> AccionCita("Confirmar", onConfirmar)
                            EstadoCita.CONFIRMADA.name -> AccionCita("Atender", onAtender)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccionCita(texto: String, onClick: () -> Unit) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(32.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(texto, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AgendaVacia() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.EventBusy,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "No hay citas para este día",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size < 2) nombre else "${partes.first()} ${partes.last().first()}."
}
