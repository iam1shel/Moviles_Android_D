package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.DatoGrafico

@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat().coerceAtLeast(1f)
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(110.dp)) {
                val stroke = Stroke(width = size.minDimension * 0.18f, cap = StrokeCap.Butt)
                var start = -90f
                datos.forEach { d ->
                    val sweep = 360f * (d.valor / total)
                    drawArc(
                        color = d.color,
                        startAngle = start,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = stroke,
                        size = Size(size.minDimension, size.minDimension),
                        topLeft = Offset.Zero
                    )
                    start += sweep
                }
            }
            Text("${total.toInt()}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            datos.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(Modifier.size(10.dp).clip(CircleShape)) {
                        drawCircle(d.color)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("${d.etiqueta}  ${d.valor.toInt()}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
