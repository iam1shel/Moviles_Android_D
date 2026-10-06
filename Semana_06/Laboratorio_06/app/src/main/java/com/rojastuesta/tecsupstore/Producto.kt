package com.rojastuesta.tecsupstore

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val precio: String
)

val catalogoTecsup = listOf(
    Producto(1, "Polo TECSUP", "Ropa", "S/ 45.00"),
    Producto(2, "Hoodie Campus", "Ropa", "S/ 120.00"),
    Producto(3, "Cuaderno de laboratorio", "Útiles", "S/ 12.50"),
    Producto(4, "Taza del taller", "Hogar", "S/ 25.00"),
    Producto(5, "Mochila técnica", "Accesorios", "S/ 89.90"),
    Producto(6, "Audífonos de estudio", "Tecnología", "S/ 65.00")
)
