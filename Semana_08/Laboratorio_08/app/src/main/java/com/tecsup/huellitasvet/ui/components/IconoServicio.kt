package com.tecsup.huellitasvet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size

@Composable
fun IconoServicio(icono: String, tamano: Dp = 24.dp) {
    val image = when (icono) {
        "syringe" -> Icons.Outlined.Vaccines
        "scissors" -> Icons.Outlined.ContentCut
        else -> Icons.Outlined.Favorite
    }
    Icon(
        imageVector = image,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(tamano)
    )
}
