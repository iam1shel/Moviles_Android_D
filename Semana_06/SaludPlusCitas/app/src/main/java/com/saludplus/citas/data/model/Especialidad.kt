package com.saludplus.citas.data.model

data class Especialidad(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val destacada: Boolean = false,
    val colorFondo: Long = 0xFFE3F2FD,
    val colorIcono: Long = 0xFF1E88E5
)
