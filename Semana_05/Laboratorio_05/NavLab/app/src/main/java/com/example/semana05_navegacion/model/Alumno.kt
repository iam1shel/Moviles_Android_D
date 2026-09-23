package com.example.semana05_navegacion.model

import androidx.compose.ui.graphics.Color

data class Alumno(
    val id: Int,
    val nombre: String,
    val carrera: String,
    val codigo: String,
    val correo: String,
    val facultad: String,
    val biografia: String,
    val iniciales: String,
    val avatar: Color
)

val alumnos = listOf(
    Alumno(
        id = 1,
        nombre = "Juan León",
        carrera = "Ingeniería de Sistemas",
        codigo = "2024-0001",
        correo = "juan.leon@example.com",
        facultad = "Ingeniería y Tecnología",
        biografia = "Estudiante destacado con interés en desarrollo Android.",
        iniciales = "JL",
        avatar = Color(0xFF6E56C8)
    ),
    Alumno(
        id = 2,
        nombre = "María García",
        carrera = "Arquitectura",
        codigo = "2024-0002",
        correo = "maria.garcia@example.com",
        facultad = "Facultad de Diseño",
        biografia = "Interesada en diseño de espacios urbanos y arquitectura sostenible.",
        iniciales = "MG",
        avatar = Color(0xFF00897B)
    ),
    Alumno(
        id = 3,
        nombre = "Carlos Perez",
        carrera = "Medicina",
        codigo = "2024-0003",
        correo = "carlos.perez@example.com",
        facultad = "Ciencias de la Salud",
        biografia = "Enfocado en medicina general y atención primaria.",
        iniciales = "CP",
        avatar = Color(0xFF1565C0)
    ),
    Alumno(
        id = 4,
        nombre = "Ana Lopez",
        carrera = "Derecho",
        codigo = "2024-0004",
        correo = "ana.lopez@example.com",
        facultad = "Facultad de Derecho",
        biografia = "Estudia derecho corporativo y resolución de conflictos.",
        iniciales = "AL",
        avatar = Color(0xFFC2185B)
    ),
    Alumno(
        id = 5,
        nombre = "Luis Ramírez",
        carrera = "Administración",
        codigo = "2024-0005",
        correo = "luis.ramirez@example.com",
        facultad = "Facultad de Negocios",
        biografia = "Orientado a gestión de proyectos y administración de empresas.",
        iniciales = "LR",
        avatar = Color(0xFFEF6C00)
    )
)

fun alumnoPorId(itemId: Int): Alumno? = alumnos.find { it.id == itemId }
