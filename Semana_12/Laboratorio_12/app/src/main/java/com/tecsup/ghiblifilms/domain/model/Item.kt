package com.tecsup.ghiblifilms.domain.model

data class Item(
    val id: String,
    val titulo: String,
    val director: String,
    val anio: String,
    val puntaje: Int,
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    val tituloOriginal: String? = null,
    val productor: String? = null,
    val duracionMin: Int? = null,
    val descripcion: String? = null,
    val bannerUrl: String? = null
)
