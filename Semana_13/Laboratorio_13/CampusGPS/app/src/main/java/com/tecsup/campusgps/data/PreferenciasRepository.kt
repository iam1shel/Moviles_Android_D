package com.tecsup.campusgps.data

import kotlinx.coroutines.flow.Flow

interface PreferenciasRepository {
    val ultimoLugarSeleccionado: Flow<Int?>
    suspend fun guardarUltimoLugar(id: Int?)
}
