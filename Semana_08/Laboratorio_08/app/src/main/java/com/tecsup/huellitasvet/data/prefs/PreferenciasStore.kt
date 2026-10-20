package com.tecsup.huellitasvet.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("ajustes")

class PreferenciasStore(private val context: Context) {

    private val perfilActivoKey = stringPreferencesKey("perfilActivo")
    private val usuarioIdKey = intPreferencesKey("usuarioId")
    private val modoOscuroKey = booleanPreferencesKey("modoOscuro")
    private val notificacionesKey = booleanPreferencesKey("notificaciones")
    private val aceptarCitasKey = booleanPreferencesKey("aceptarCitas")

    val perfilActivo: Flow<String> = context.dataStore.data.map { it[perfilActivoKey] ?: "" }
    val usuarioId: Flow<Int> = context.dataStore.data.map { it[usuarioIdKey] ?: 0 }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[modoOscuroKey] ?: false }
    val notificaciones: Flow<Boolean> = context.dataStore.data.map { it[notificacionesKey] ?: true }
    val aceptarCitas: Flow<Boolean> = context.dataStore.data.map { it[aceptarCitasKey] ?: true }

    suspend fun guardarSesion(rol: String, usuarioId: Int) {
        context.dataStore.edit {
            it[perfilActivoKey] = rol
            it[usuarioIdKey] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { it[modoOscuroKey] = b }
    }

    suspend fun guardarNotificaciones(b: Boolean) {
        context.dataStore.edit { it[notificacionesKey] = b }
    }

    suspend fun guardarAceptarCitas(b: Boolean) {
        context.dataStore.edit { it[aceptarCitasKey] = b }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it[perfilActivoKey] = ""
            it[usuarioIdKey] = 0
        }
    }
}
