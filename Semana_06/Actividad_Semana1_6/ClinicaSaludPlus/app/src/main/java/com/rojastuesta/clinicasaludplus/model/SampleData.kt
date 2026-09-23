package com.rojastuesta.clinicasaludplus.model

import androidx.compose.runtime.mutableStateListOf

object SampleData {
    val doctors = listOf(
        Doctor(
            id = "1",
            name = "Dra. Ana Torres",
            specialty = "Cardióloga",
            rating = 4.9,
            reviewCount = 128,
            yearsExperience = 12,
            description = "Especialista en cardiología clínica y prevención cardiovascular. Amplia experiencia en evaluación y diagnóstico de patologías cardíacas.",
            initials = "AT"
        ),
        Doctor(
            id = "2",
            name = "Dr. Luis Vega",
            specialty = "Pediatra",
            rating = 4.7,
            reviewCount = 95,
            yearsExperience = 8,
            description = "Médico especialista en pediatría y desarrollo infantil. Atención integral desde recién nacidos hasta adolescentes con enfoque empático.",
            initials = "LV"
        ),
        Doctor(
            id = "3",
            name = "Dra. Rosa Díaz",
            specialty = "Dermatóloga",
            rating = 4.8,
            reviewCount = 110,
            yearsExperience = 10,
            description = "Dermatóloga clínica y estética. Experta en salud cutánea, tratamiento de afecciones de la piel y procedimientos dermatológicos.",
            initials = "RD"
        )
    )

    // In-memory appointments list initialized with sample data
    val appointments = mutableStateListOf(
        Appointment(
            id = "app_1",
            doctorName = "Dra. Ana Torres",
            specialty = "Cardióloga",
            date = "Viernes 27",
            time = "10:30 am",
            status = "Confirmada"
        ),
        Appointment(
            id = "app_2",
            doctorName = "Dr. Luis Vega",
            specialty = "Pediatra",
            date = "Miércoles 15",
            time = "3:00 pm",
            status = "Completada"
        )
    )

    val medicalHistory = listOf(
        Triple("Dr. Luis Vega", "Pediatría — Control anual", "15 Nov 2024"),
        Triple("Dra. Rosa Díaz", "Dermatología — Dermatitis de contacto", "02 Oct 2024"),
        Triple("Dra. Ana Torres", "Cardiología — Electrocardiograma preventivo", "10 Ago 2024")
    )

    fun getDoctorById(id: String): Doctor? {
        return doctors.find { it.id == id }
    }

    fun addAppointment(doctorName: String, specialty: String, date: String, time: String) {
        appointments.add(
            0,
            Appointment(
                id = "app_${System.currentTimeMillis()}",
                doctorName = doctorName,
                specialty = specialty,
                date = date,
                time = time,
                status = "Confirmada"
            )
        )
    }
}
