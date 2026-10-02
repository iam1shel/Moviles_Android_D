package com.rojastuesta.tecsupstore

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: String
)

val catalogoTecsup = listOf(
    Producto(1, "Audifonos", "S/ 89.00"),
    Producto(2, "Smartwatch", "S/ 199.00"),
    Producto(3, "Funda celular", "S/ 25.00")
)
