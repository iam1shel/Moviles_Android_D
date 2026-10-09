package com.saludplus.citas.data.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val clave: String
)
