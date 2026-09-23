package com.rojastuesta.tecsupfit.model

enum class ReservationStatus {
    CONFIRMADA,
    COMPLETADA
}

data class Reservation(
    val id: String,
    val className: String,
    val timeInfo: String,
    val room: String,
    val status: ReservationStatus
)
