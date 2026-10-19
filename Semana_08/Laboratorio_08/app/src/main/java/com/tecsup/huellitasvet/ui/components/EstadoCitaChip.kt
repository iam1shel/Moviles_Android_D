package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.theme.AtendidaColor
import com.tecsup.huellitasvet.ui.theme.CanceladaColor
import com.tecsup.huellitasvet.ui.theme.ConfirmadaColor
import com.tecsup.huellitasvet.ui.theme.PendienteColor

@Composable
fun EstadoCitaChip(
    estado: String,
    modifier: Modifier = Modifier,
    sobreOscuro: Boolean = false
) {
    val (color, texto) = when (estado) {
        EstadoCita.PENDIENTE.name -> PendienteColor to "Pendiente"
        EstadoCita.CONFIRMADA.name -> ConfirmadaColor to "Confirmada"
        EstadoCita.ATENDIDA.name -> AtendidaColor to "Atendida"
        EstadoCita.CANCELADA.name -> CanceladaColor to "Cancelada"
        else -> AtendidaColor to estado
    }
    val fondo = if (sobreOscuro) Color.White.copy(alpha = 0.22f) else color.copy(alpha = 0.15f)
    val textoColor = if (sobreOscuro) Color.White else color
    Surface(
        modifier = modifier,
        color = fondo,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = texto,
            color = textoColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
