package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie

private enum class EstiloAvatar { LUNA, MAX, ROCKY, MICHI, PERRO, GATO, OTRO }

private fun estiloDe(nombre: String, especie: String): EstiloAvatar = when (nombre.lowercase()) {
    "luna" -> EstiloAvatar.LUNA
    "max" -> EstiloAvatar.MAX
    "rocky" -> EstiloAvatar.ROCKY
    "michi" -> EstiloAvatar.MICHI
    else -> when (especie) {
        Especie.PERRO.name -> EstiloAvatar.PERRO
        Especie.GATO.name -> EstiloAvatar.GATO
        else -> EstiloAvatar.OTRO
    }
}

fun fondoAvatar(nombre: String, especie: String): Color = when (estiloDe(nombre, especie)) {
    EstiloAvatar.LUNA -> Color(0xFFE7F4EE)
    EstiloAvatar.MAX -> Color(0xFFF8E8D0)
    EstiloAvatar.ROCKY -> Color(0xFFE4EAF3)
    EstiloAvatar.MICHI -> Color(0xFFFDE8D0)
    EstiloAvatar.PERRO -> Color(0xFFF8E8D0)
    EstiloAvatar.GATO -> Color(0xFFE7F4EE)
    EstiloAvatar.OTRO -> Color(0xFFEDE9FE)
}

@Composable
fun MascotaAvatar(
    especie: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp,
    fondo: Color = Color.Unspecified,
    nombre: String = "",
    mostrarPlus: Boolean = false
) {
    val estilo = estiloDe(nombre, especie)
    val bg = if (fondo != Color.Unspecified) fondo else fondoAvatar(nombre, especie)
    Box(modifier = modifier.size(tamano), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(bg)
        ) {
            Canvas(Modifier.fillMaxSize()) {
                when (estilo) {
                    EstiloAvatar.LUNA -> dibujarLuna()
                    EstiloAvatar.MAX, EstiloAvatar.PERRO -> dibujarMax()
                    EstiloAvatar.ROCKY -> dibujarRocky()
                    EstiloAvatar.MICHI -> dibujarMichi()
                    EstiloAvatar.GATO -> dibujarLuna()
                    EstiloAvatar.OTRO -> dibujarOtro()
                }
            }
        }
        if (mostrarPlus) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(tamano * 0.28f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(tamano * 0.18f)
                )
            }
        }
    }
}

private fun DrawScope.dibujarLuna() {
    val w = size.width
    val h = size.height
    val pelaje = Color(0xFFB7C4C8)
    val hocico = Color(0xFFF4F7F8)
    val orejaIn = Color(0xFFF4B6C8)
    val ojo = Color(0xFF2F8A55)
    oreja(w * 0.30f, h * 0.20f, w * 0.18f, h * 0.28f, pelaje, orejaIn, izquierda = true)
    oreja(w * 0.70f, h * 0.20f, w * 0.18f, h * 0.28f, pelaje, orejaIn, izquierda = false)
    drawCircle(pelaje, radius = w * 0.32f, center = Offset(w * 0.50f, h * 0.58f))
    drawCircle(hocico, radius = w * 0.16f, center = Offset(w * 0.50f, h * 0.68f))
    drawCircle(ojo, radius = w * 0.045f, center = Offset(w * 0.40f, h * 0.54f))
    drawCircle(ojo, radius = w * 0.045f, center = Offset(w * 0.60f, h * 0.54f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.415f, h * 0.525f))
    drawCircle(Color.White, radius = w * 0.015f, center = Offset(w * 0.615f, h * 0.525f))
    drawCircle(Color(0xFFF9A8D4), radius = w * 0.035f, center = Offset(w * 0.50f, h * 0.66f))
    drawLine(Color(0xFF94A3B8), Offset(w * 0.18f, h * 0.64f), Offset(w * 0.36f, h * 0.68f), 2.5f, StrokeCap.Round)
    drawLine(Color(0xFF94A3B8), Offset(w * 0.82f, h * 0.64f), Offset(w * 0.64f, h * 0.68f), 2.5f, StrokeCap.Round)
}

private fun DrawScope.dibujarMax() {
    val w = size.width
    val h = size.height
    val pelaje = Color(0xFFE2B36A)
    val oreja = Color(0xFFC9893A)
    val hocico = Color(0xFFF6E0B8)
    drawCircle(oreja, radius = w * 0.16f, center = Offset(w * 0.26f, h * 0.38f))
    drawCircle(oreja, radius = w * 0.16f, center = Offset(w * 0.74f, h * 0.38f))
    drawCircle(pelaje, radius = w * 0.34f, center = Offset(w * 0.50f, h * 0.56f))
    drawCircle(hocico, radius = w * 0.16f, center = Offset(w * 0.50f, h * 0.68f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.042f, center = Offset(w * 0.40f, h * 0.52f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.042f, center = Offset(w * 0.60f, h * 0.52f))
    drawCircle(Color.White, radius = w * 0.014f, center = Offset(w * 0.415f, h * 0.505f))
    drawCircle(Color.White, radius = w * 0.014f, center = Offset(w * 0.615f, h * 0.505f))
    drawCircle(Color(0xFF6B3F1F), radius = w * 0.05f, center = Offset(w * 0.50f, h * 0.64f))
    drawArc(
        color = Color(0xFF6B3F1F),
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(w * 0.40f, h * 0.68f),
        size = Size(w * 0.20f, h * 0.10f),
        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.dibujarRocky() {
    val w = size.width
    val h = size.height
    val pelaje = Color(0xFF8B97A8)
    val hocico = Color(0xFFC5CDD6)
    drawCircle(pelaje, radius = w * 0.14f, center = Offset(w * 0.28f, h * 0.34f))
    drawCircle(pelaje, radius = w * 0.14f, center = Offset(w * 0.72f, h * 0.34f))
    drawOval(pelaje, topLeft = Offset(w * 0.18f, h * 0.32f), size = Size(w * 0.64f, h * 0.52f))
    drawOval(hocico, topLeft = Offset(w * 0.32f, h * 0.54f), size = Size(w * 0.36f, h * 0.24f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.40f, h * 0.50f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.60f, h * 0.50f))
    drawCircle(Color(0xFF334155), radius = w * 0.045f, center = Offset(w * 0.50f, h * 0.62f))
    drawLine(Color(0xFF64748B), Offset(w * 0.34f, h * 0.44f), Offset(w * 0.66f, h * 0.44f), 2.5f, StrokeCap.Round)
}

private fun DrawScope.dibujarMichi() {
    val w = size.width
    val h = size.height
    val pelaje = Color(0xFFF0A04B)
    val raya = Color(0xFFD97706)
    val orejaIn = Color(0xFFF8B4A0)
    oreja(w * 0.30f, h * 0.20f, w * 0.18f, h * 0.28f, pelaje, orejaIn, true)
    oreja(w * 0.70f, h * 0.20f, w * 0.18f, h * 0.28f, pelaje, orejaIn, false)
    drawCircle(pelaje, radius = w * 0.32f, center = Offset(w * 0.50f, h * 0.58f))
    drawCircle(raya, radius = w * 0.04f, center = Offset(w * 0.42f, h * 0.40f))
    drawCircle(raya, radius = w * 0.04f, center = Offset(w * 0.58f, h * 0.40f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.042f, center = Offset(w * 0.40f, h * 0.54f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.042f, center = Offset(w * 0.60f, h * 0.54f))
    drawCircle(Color.White, radius = w * 0.014f, center = Offset(w * 0.415f, h * 0.525f))
    drawCircle(Color(0xFFF9A8D4), radius = w * 0.035f, center = Offset(w * 0.50f, h * 0.66f))
}

private fun DrawScope.dibujarOtro() {
    val w = size.width
    val h = size.height
    drawOval(Color(0xFFA78BFA), topLeft = Offset(w * 0.22f, h * 0.28f), size = Size(w * 0.56f, h * 0.50f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.40f, h * 0.48f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(w * 0.60f, h * 0.48f))
}

private fun DrawScope.oreja(
    cx: Float, cy: Float, base: Float, alto: Float,
    pelaje: Color, interior: Color, izquierda: Boolean
) {
    val dir = if (izquierda) -1f else 1f
    val path = Path().apply {
        moveTo(cx - base * 0.4f * dir, cy + alto * 0.55f)
        lineTo(cx + dir * base * 0.15f, cy - alto * 0.05f)
        lineTo(cx + dir * base * 0.55f, cy + alto * 0.55f)
        close()
    }
    drawPath(path, pelaje, style = Fill)
    val inner = Path().apply {
        moveTo(cx - base * 0.15f * dir, cy + alto * 0.45f)
        lineTo(cx + dir * base * 0.12f, cy + alto * 0.12f)
        lineTo(cx + dir * base * 0.32f, cy + alto * 0.45f)
        close()
    }
    drawPath(inner, interior, style = Fill)
}
