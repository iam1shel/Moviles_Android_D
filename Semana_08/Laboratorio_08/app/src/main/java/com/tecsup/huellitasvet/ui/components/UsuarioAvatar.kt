package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun UsuarioAvatar(
    nombre: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp
) {
    val clave = nombre.lowercase()
    val fondo = when {
        clave.contains("ana") -> Color(0xFFD1FAE5)
        clave.contains("luis") -> Color(0xFFDBEAFE)
        else -> Color(0xFFE0E7FF)
    }
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(fondo)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            when {
                clave.contains("ana") -> dibujarAna()
                clave.contains("luis") -> dibujarLuis()
                else -> dibujarDoctora()
            }
        }
    }
}

private fun DrawScope.dibujarAna() {
    val w = size.width
    val h = size.height
    val piel = Color(0xFFF2C7A0)
    val pelo = Color(0xFF4A2E1A)
    val ropa = Color(0xFF059669)
    drawCircle(pelo, radius = w * 0.42f, center = Offset(w * 0.50f, h * 0.42f))
    drawCircle(piel, radius = w * 0.28f, center = Offset(w * 0.50f, h * 0.46f))
    drawOval(pelo, topLeft = Offset(w * 0.18f, h * 0.18f), size = Size(w * 0.64f, h * 0.28f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.035f, center = Offset(w * 0.40f, h * 0.46f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.035f, center = Offset(w * 0.60f, h * 0.46f))
    drawCircle(Color(0xFFF9A8D4), radius = w * 0.03f, center = Offset(w * 0.33f, h * 0.54f))
    drawCircle(Color(0xFFF9A8D4), radius = w * 0.03f, center = Offset(w * 0.67f, h * 0.54f))
    drawArc(
        color = Color(0xFFB45309),
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(w * 0.40f, h * 0.52f),
        size = Size(w * 0.20f, h * 0.10f),
        style = Stroke(width = 3f)
    )
    drawOval(ropa, topLeft = Offset(w * 0.18f, h * 0.78f), size = Size(w * 0.64f, h * 0.30f))
}

private fun DrawScope.dibujarLuis() {
    val w = size.width
    val h = size.height
    val piel = Color(0xFFE8B894)
    val pelo = Color(0xFF1C1917)
    val ropa = Color(0xFF1D4ED8)
    drawCircle(piel, radius = w * 0.28f, center = Offset(w * 0.50f, h * 0.46f))
    drawOval(pelo, topLeft = Offset(w * 0.22f, h * 0.16f), size = Size(w * 0.56f, h * 0.22f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.035f, center = Offset(w * 0.40f, h * 0.45f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.035f, center = Offset(w * 0.60f, h * 0.45f))
    drawOval(Color(0xFF44403C), topLeft = Offset(w * 0.34f, h * 0.56f), size = Size(w * 0.32f, h * 0.12f))
    drawOval(ropa, topLeft = Offset(w * 0.16f, h * 0.78f), size = Size(w * 0.68f, h * 0.30f))
}

private fun DrawScope.dibujarDoctora() {
    val w = size.width
    val h = size.height
    val piel = Color(0xFFF0C7A8)
    val pelo = Color(0xFF292524)
    val bata = Color(0xFFDBEAFE)
    drawCircle(pelo, radius = w * 0.40f, center = Offset(w * 0.50f, h * 0.40f))
    drawCircle(piel, radius = w * 0.27f, center = Offset(w * 0.50f, h * 0.46f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.032f, center = Offset(w * 0.40f, h * 0.45f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.032f, center = Offset(w * 0.60f, h * 0.45f))
    drawCircle(Color.White.copy(alpha = 0.35f), radius = w * 0.07f, center = Offset(w * 0.40f, h * 0.45f))
    drawCircle(Color.White.copy(alpha = 0.35f), radius = w * 0.07f, center = Offset(w * 0.60f, h * 0.45f))
    drawLine(Color(0xFF1E293B), Offset(w * 0.47f, h * 0.45f), Offset(w * 0.53f, h * 0.45f), 3f)
    drawOval(bata, topLeft = Offset(w * 0.16f, h * 0.76f), size = Size(w * 0.68f, h * 0.32f))
    val cruz = Path().apply {
        moveTo(w * 0.47f, h * 0.84f)
        lineTo(w * 0.53f, h * 0.84f)
        lineTo(w * 0.53f, h * 0.90f)
        lineTo(w * 0.59f, h * 0.90f)
        lineTo(w * 0.59f, h * 0.96f)
        lineTo(w * 0.53f, h * 0.96f)
        lineTo(w * 0.53f, h * 1.02f)
        lineTo(w * 0.47f, h * 1.02f)
        lineTo(w * 0.47f, h * 0.96f)
        lineTo(w * 0.41f, h * 0.96f)
        lineTo(w * 0.41f, h * 0.90f)
        lineTo(w * 0.47f, h * 0.90f)
        close()
    }
    drawPath(cruz, Color(0xFFDC2626), style = Fill)
}
