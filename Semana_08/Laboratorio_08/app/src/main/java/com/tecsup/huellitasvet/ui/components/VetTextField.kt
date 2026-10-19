package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun VetTextField(
    valor: String,
    onValor: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValor,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = icono?.let { { Icon(it, contentDescription = null, tint = Color(0xFF94A3B8)) } },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE2E8F0),
            focusedBorderColor = Color(0xFFCBD5E1),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedLabelColor = Color(0xFF94A3B8),
            focusedLabelColor = Color(0xFF94A3B8),
            errorBorderColor = Color(0xFFDC2626)
        )
    )
}
