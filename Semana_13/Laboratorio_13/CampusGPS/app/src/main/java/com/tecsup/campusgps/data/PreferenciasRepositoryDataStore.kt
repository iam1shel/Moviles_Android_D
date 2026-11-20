package com.tecsup.campusgps.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_campus")

class PreferenciasRepositoryDataStore(private val context: Context) : PreferenciasRepository {
    private object Keys {
        val ULTIMO_LUGAR = intPreferencesKey("ultimo_lugar_id")
    }

    override val ultimoLugarSeleccionado: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[Keys.ULTIMO_LUGAR]
        }

    override suspend fun guardarUltimoLugar(id: Int?) {
        context.dataStore.edit { preferences ->
            if (id == null) {
                preferences.remove(Keys.ULTIMO_LUGAR)
            } else {
                preferences[Keys.ULTIMO_LUGAR] = id
            }
        }
    }
}
