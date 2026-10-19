package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outlined: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    val shape = RoundedCornerShape(16.dp)
    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.fillMaxWidth().height(52.dp),
            shape = shape,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = containerColor),
            border = BorderStroke(1.5.dp, containerColor)
        ) { Text(texto, fontWeight = FontWeight.SemiBold) }
    } else {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.fillMaxWidth().height(52.dp),
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = containerColor)
        ) { Text(texto, fontWeight = FontWeight.SemiBold) }
    }
}
