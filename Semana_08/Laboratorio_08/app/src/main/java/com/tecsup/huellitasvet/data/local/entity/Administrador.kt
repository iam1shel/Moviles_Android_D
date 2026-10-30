package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tecsup.huellitasvet.model.Rol

@Entity(tableName = "administradores")
data class Administrador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigo: String,
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val clave: String
) {
    fun nombreCompleto(): String =
        listOf(nombre, apellidos).filter { it.isNotBlank() }.joinToString(" ")

    fun comoUsuario(): Usuario = Usuario(
        id = id,
        nombre = nombre,
        correo = correo,
        clave = clave,
        rol = Rol.ADMINISTRADOR.name,
        codigo = codigo,
        apellidos = apellidos
    )
}
