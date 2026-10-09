package com.saludplus.citas.data.model

import java.time.DayOfWeek

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val rating: Double,
    val experienciaAnios: Int,
    val reseñas: Int = 100,
    val disponibilidad: String = "Disponible hoy",
    val cmp: String = "12345",
    /** Días de la semana en que atiende. */
    val diasAtencion: Set<DayOfWeek> = setOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    ),
    /** Horarios base del médico (no todos atienden las mismas horas). */
    val horariosAtencion: List<String> = listOf(
        "09:00", "09:30", "10:00", "10:30", "11:00"
    )
)
