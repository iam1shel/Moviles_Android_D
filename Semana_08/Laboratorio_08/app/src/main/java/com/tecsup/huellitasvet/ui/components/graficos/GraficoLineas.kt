package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.PuntoPeso

@Composable
fun GraficoLineas(puntos: List<PuntoPeso>, modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.primary
    Column(modifier.fillMaxWidth().padding(8.dp)) {
        if (puntos.isEmpty()) {
            Text("Aún no hay pesos registrados", style = MaterialTheme.typography.bodySmall)
            return
        }
        val min = puntos.minOf { it.peso }.toFloat()
        val max = puntos.maxOf { it.peso }.toFloat().coerceAtLeast(min + 0.1f)
        Canvas(Modifier.fillMaxWidth().height(140.dp).padding(8.dp)) {
            val stepX = if (puntos.size == 1) size.width / 2 else size.width / (puntos.size - 1)
            val coords = puntos.mapIndexed { i, p ->
                val x = if (puntos.size == 1) size.width / 2 else i * stepX
                val y = size.height - ((p.peso.toFloat() - min) / (max - min)) * size.height
                Offset(x, y)
            }
            val path = Path().apply {
                coords.forEachIndexed { i, o -> if (i == 0) moveTo(o.x, o.y) else lineTo(o.x, o.y) }
            }
            drawPath(path, color, style = Stroke(width = 6f, cap = StrokeCap.Round))
            coords.forEach { drawCircle(color, radius = 8f, center = it) }
        }
        Text(
            "${puntos.first().fecha}  →  ${puntos.last().fecha}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
