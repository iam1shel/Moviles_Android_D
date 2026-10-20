package com.tecsup.huellitasvet.model

import androidx.compose.ui.graphics.Color

data class DatoGrafico(
    val etiqueta: String,
    val valor: Float,
    val color: Color
)

data class PuntoPeso(
    val fecha: String,
    val peso: Double
)
