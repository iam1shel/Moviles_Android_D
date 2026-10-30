package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tecsup.huellitasvet.data.local.entity.Administrador

@Dao
interface AdministradorDao {
    @Query("SELECT * FROM administradores WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Administrador?

    @Query("SELECT * FROM administradores WHERE lower(correo) = lower(:correo) LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Administrador?

    @Query("SELECT * FROM administradores WHERE id = :id")
    suspend fun obtener(id: Int): Administrador?

    @Insert
    suspend fun insertar(administrador: Administrador): Long

    @Query("SELECT COUNT(*) FROM administradores")
    suspend fun contar(): Int
}
