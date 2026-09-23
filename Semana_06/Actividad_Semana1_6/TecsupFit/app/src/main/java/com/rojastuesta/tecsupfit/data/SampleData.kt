package com.rojastuesta.tecsupfit.data

import com.rojastuesta.tecsupfit.model.GymClass
import com.rojastuesta.tecsupfit.model.Reservation
import com.rojastuesta.tecsupfit.model.ReservationStatus
import com.rojastuesta.tecsupfit.model.Routine

object SampleData {
    val sampleClasses = listOf(
        GymClass(
            id = "yoga_funcional",
            name = "Yoga funcional",
            time = "7:00 am",
            room = "Sala 2",
            duration = "50 min",
            description = "Sesión para mejorar la flexibilidad, movilidad articular y equilibrio corporal.",
            availableSpots = 10,
            totalSpots = 15,
            category = "Hoy",
            iconType = "YOGA"
        ),
        GymClass(
            id = "cross_training",
            name = "Cross Training",
            time = "6:00 pm",
            room = "Sala 1",
            duration = "45 min",
            description = "Entrenamiento funcional de alta intensidad para la mejora de la fuerza y resistencia.",
            availableSpots = 8,
            totalSpots = 12,
            category = "Hoy",
            iconType = "CROSS"
        ),
        GymClass(
            id = "spinning",
            name = "Spinning",
            time = "7:30 pm",
            room = "Sala 3",
            duration = "45 min",
            description = "Ejercicio cardiovascular sobre bicicleta estática al ritmo de la música.",
            availableSpots = 5,
            totalSpots = 15,
            category = "Hoy",
            iconType = "SPINNING"
        )
    )

    val initialReservations = listOf(
        Reservation(
            id = "res_1",
            className = "Cross Training",
            timeInfo = "Hoy, 6:00 pm",
            room = "Sala 1",
            status = ReservationStatus.CONFIRMADA
        ),
        Reservation(
            id = "res_2",
            className = "Yoga funcional",
            timeInfo = "Ayer, 7:00 am",
            room = "Sala 2",
            status = ReservationStatus.COMPLETADA
        )
    )

    val sampleRoutines = listOf(
        Routine(
            id = "fuerza_basica",
            name = "Fuerza básica",
            type = "Fuerza",
            duration = "30 min",
            exercisesCount = 5,
            level = "Principiante"
        ),
        Routine(
            id = "cardio_burn",
            name = "Cardio",
            type = "Resistencia",
            duration = "25 min",
            exercisesCount = 6,
            level = "Intermedio"
        ),
        Routine(
            id = "tren_superior",
            name = "Tren superior",
            type = "Musculación",
            duration = "35 min",
            exercisesCount = 7,
            level = "Avanzado"
        )
    )

    fun getClassById(id: String): GymClass {
        return sampleClasses.find { it.id == id } ?: sampleClasses.first()
    }
}
