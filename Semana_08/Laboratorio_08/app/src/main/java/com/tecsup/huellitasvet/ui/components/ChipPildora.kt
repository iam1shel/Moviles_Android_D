package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChipPildora(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo = if (seleccionado) MaterialTheme.colorScheme.primary else Color(0xFFF1F5F9)
    val color = if (seleccionado) MaterialTheme.colorScheme.onPrimary else Color(0xFF94A3B8)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(fondo)
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SelectorRol(
    dueño: Boolean,
    onDueño: () -> Unit,
    onVet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RolOpcion("Dueño", dueño, onDueño, Modifier.weight(1f))
        RolOpcion("Veterinario", !dueño, onVet, Modifier.weight(1f))
    }
}

@Composable
private fun RolOpcion(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (seleccionado) MaterialTheme.colorScheme.primary else Color(0xFFF1F5F9))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            texto,
            color = if (seleccionado) MaterialTheme.colorScheme.onPrimary else Color(0xFF94A3B8),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}
