package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HoraChip(
    hora: String,
    seleccionado: Boolean,
    ocupado: Boolean,
    deshabilitado: Boolean,
    onClick: () -> Unit
) {
    val inactivo = ocupado || deshabilitado
    FilterChip(
        selected = seleccionado && !inactivo,
        onClick = { if (!inactivo) onClick() },
        enabled = !inactivo,
        label = {
            Text(
                hora,
                modifier = if (ocupado) Modifier.drawWithContent {
                    drawContent()
                    drawLine(
                        Color.Gray,
                        Offset(0f, size.height / 2),
                        Offset(size.width, size.height / 2),
                        strokeWidth = 3f
                    )
                } else Modifier
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = Color(0xFFE2E8F0),
            disabledLabelColor = Color(0xFF94A3B8)
        ),
        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
    )
}
