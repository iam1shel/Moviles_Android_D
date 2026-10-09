package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.SaludPlusPrimaryButton
import com.saludplus.citas.ui.components.SaludPlusTopBar
import com.saludplus.citas.ui.components.TarjetaMedicoResumen
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.AzulTexto
import com.saludplus.citas.ui.theme.GrisBorde
import com.saludplus.citas.ui.theme.GrisFondo
import java.time.LocalDate

@Composable
fun FechaHoraScreen(
    especialidadId: Int,
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    var semanaInicio by remember { mutableStateOf(CalendarioUtils.lunesDeSemana(LocalDate.now())) }
    val dias = remember(semanaInicio) { CalendarioUtils.diasHabilesDeSemana(semanaInicio) }
    var fechaSeleccionada by remember {
        mutableStateOf(CalendarioUtils.toIso(dias.first()))
    }
    var hora by remember { mutableStateOf<String?>(null) }

    if (dias.none { CalendarioUtils.toIso(it) == fechaSeleccionada }) {
        fechaSeleccionada = CalendarioUtils.toIso(dias.first())
        hora = null
    }

    val horarios = Repositorio.horariosDisponibles(medicoId, fechaSeleccionada)
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            SaludPlusTopBar(title = "Seleccionar fecha y hora", onVolver = onVolver)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (medico != null) {
                TarjetaMedicoResumen(
                    medico = medico,
                    especialidad = especialidad?.nombre ?: "",
                    cmp = null
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        if (CalendarioUtils.puedeRetroceder(semanaInicio)) {
                            semanaInicio = semanaInicio.minusWeeks(1)
                            hora = null
                        }
                    },
                    enabled = CalendarioUtils.puedeRetroceder(semanaInicio)
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                }
                Text(
                    text = CalendarioUtils.tituloMesAnio(semanaInicio),
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto,
                    fontSize = 18.sp
                )
                IconButton(
                    onClick = {
                        semanaInicio = semanaInicio.plusWeeks(1)
                        hora = null
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(dias, key = { it.toString() }) { dia ->
                    val iso = CalendarioUtils.toIso(dia)
                    val selected = fechaSeleccionada == iso
                    Column(
                        modifier = Modifier
                            .width(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (selected) AzulPrimario else Color.White)
                            .border(
                                1.dp,
                                if (selected) AzulPrimario else GrisBorde,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                fechaSeleccionada = iso
                                hora = null
                            }
                            .padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val partes = CalendarioUtils.etiquetaCorta(dia).split(" ")
                        Text(
                            text = partes.getOrElse(0) { "" },
                            color = if (selected) Color.White else AzulTexto,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = partes.getOrElse(1) { "" },
                            color = if (selected) Color.White else AzulTexto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(horarios) { h ->
                    val selected = hora == h
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (selected) AzulPrimario else GrisFondo)
                            .border(
                                1.dp,
                                if (selected) AzulPrimario else GrisBorde,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { hora = h }
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = h,
                            color = if (selected) Color.White else AzulTexto,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            SaludPlusPrimaryButton(
                text = "Continuar",
                onClick = { onContinuar(fechaSeleccionada, hora!!) },
                enabled = hora != null
            )
        }
    }
}
