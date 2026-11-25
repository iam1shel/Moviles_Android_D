package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun obtenerColoresUsuario(userId: Int): Pair<Color, Color> {
    return when (userId) {
        1 -> Pair(Color(0xFFD1E4F6), Color(0xFF0B3C65)) // Azul claro
        2 -> Pair(Color(0xFFFEF3C7), Color(0xFF78350F)) // Amarillo suave
        3 -> Pair(Color(0xFFD1FAE5), Color(0xFF065F46)) // Verde claro
        4 -> Pair(Color(0xFFFCE7F3), Color(0xFF831843)) // Rosado suave
        5 -> Pair(Color(0xFFEDE9FE), Color(0xFF5B21B6)) // Lila
        else -> {
            val palette = listOf(
                Pair(Color(0xFFE0F2FE), Color(0xFF0369A1)), // Cyan pastel
                Pair(Color(0xFFFEE2E2), Color(0xFF991B1B)), // Rojo pastel
                Pair(Color(0xFFF3E8FF), Color(0xFF6B21A8)), // Morado pastel
                Pair(Color(0xFFCCFBF1), Color(0xFF115E59)), // Teal pastel
                Pair(Color(0xFFFFEDD5), Color(0xFF9A3412))  // Naranja pastel
            )
            palette[(userId - 6).coerceAtLeast(0) % palette.size]
        }
    }
}

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp,
    cornerRadius: Dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp
) = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f)
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}

@Composable
fun TarjetaPost(
    userId: Int,
    titulo: String,
    texto: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = obtenerColoresUsuario(userId)
    Card(
        onClick = alPulsar,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = bgColor,
                contentColor = textColor
            ) {
                Text(
                    text = "Usuario $userId",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = texto,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun Cargando(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text("Cargando publicaciones…", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ErrorConReintento(
    mensaje: String,
    alReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ) {
            Text(mensaje, Modifier.padding(12.dp), textAlign = TextAlign.Center)
        }
        Button(onClick = alReintentar) { Text("Reintentar") }
    }
}
