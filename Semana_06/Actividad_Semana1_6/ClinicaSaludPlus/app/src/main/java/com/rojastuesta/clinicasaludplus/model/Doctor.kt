package com.rojastuesta.clinicasaludplus.model

data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviewCount: Int,
    val yearsExperience: Int,
    val description: String,
    val initials: String
)
