package com.rojastuesta.clinicasaludplus.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Appointments : Screen("appointments")
    object MedicalHistory : Screen("medical_history")
    object Profile : Screen("profile")

    object DoctorProfile : Screen("doctor_profile/{doctorId}") {
        fun createRoute(doctorId: String) = "doctor_profile/$doctorId"
    }

    object BookAppointment : Screen("book_appointment/{doctorId}") {
        fun createRoute(doctorId: String) = "book_appointment/$doctorId"
    }

    object Confirmation : Screen("confirmation/{doctorName}/{date}/{time}") {
        fun createRoute(doctorName: String, date: String, time: String) =
            "confirmation/${doctorName}/$date/$time"
    }
}
