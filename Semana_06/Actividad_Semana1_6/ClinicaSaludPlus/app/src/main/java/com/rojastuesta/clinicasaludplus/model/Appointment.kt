package com.rojastuesta.clinicasaludplus.model

data class Appointment(
    val id: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val status: String // "Confirmada" or "Completada"
)
