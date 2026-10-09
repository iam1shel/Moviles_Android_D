package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val rating: Double,
    val experienciaAnios: Int,
    val reseñas: Int = 100,
    val disponibilidad: String = "Disponible hoy",
    val cmp: String = "12345"
)
