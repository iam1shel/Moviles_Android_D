package com.tecsup.ghiblifilms.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val director: String,
    val anio: String,
    val puntaje: Int,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)
