package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.DatoGrafico

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val max = datos.maxOfOrNull { it.valor }?.coerceAtLeast(1f) ?: 1f
    Column(modifier.fillMaxWidth().padding(8.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
            val barWidth = size.width / (datos.size * 2f).coerceAtLeast(1f)
            datos.forEachIndexed { i, d ->
                val h = (d.valor / max) * size.height * 0.85f
                val x = barWidth * 0.5f + i * barWidth * 2f
                drawRoundRect(
                    color = d.color,
                    topLeft = Offset(x, size.height - h),
                    size = Size(barWidth, h),
                    cornerRadius = CornerRadius(12f, 12f)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            datos.forEach { d ->
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${d.valor.toInt()}", style = MaterialTheme.typography.labelLarge)
                    Text(d.etiqueta, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                }
            }
        }
    }
}
