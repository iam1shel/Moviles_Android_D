package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.DatoGrafico

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    diametro: Dp = 84.dp,
    grosor: Dp = 14.dp
) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat().coerceAtLeast(1f)
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(diametro)) {
                    val anchoAnillo = grosor.toPx()
                    val stroke = Stroke(width = anchoAnillo, cap = StrokeCap.Butt)
                    val lado = size.minDimension - anchoAnillo
                    var start = -90f
                    datos.forEach { d ->
                        val sweep = 360f * (d.valor / total)
                        drawArc(
                            color = d.color,
                            startAngle = start,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = stroke,
                            size = Size(lado, lado),
                            topLeft = Offset(anchoAnillo / 2, anchoAnillo / 2)
                        )
                        start += sweep
                    }
                }
                Text(
                    "${total.toInt()}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 19.sp
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Box(modifier = Modifier.weight(2f), contentAlignment = Alignment.CenterStart) {
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                datos.forEach { d ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Canvas(Modifier.size(8.dp).clip(CircleShape)) {
                            drawCircle(d.color)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(d.etiqueta, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(16.dp))
                        Text("${d.valor.toInt()}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
