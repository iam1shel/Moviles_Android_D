package com.rojastuesta.tecsupfit.model

data class GymClass(
    val id: String,
    val name: String,
    val time: String,
    val room: String,
    val duration: String,
    val description: String,
    val availableSpots: Int,
    val totalSpots: Int,
    val category: String, // e.g. "Hoy", "Esta semana"
    val iconType: String // e.g. "YOGA", "CROSS", "SPINNING", "PILATES"
)
