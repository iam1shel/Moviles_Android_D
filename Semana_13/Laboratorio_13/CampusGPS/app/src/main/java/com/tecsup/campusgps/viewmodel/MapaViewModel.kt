package com.tecsup.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.UbicacionRepository
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.distanciaMetros
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) })
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    private val _radioSeleccionado = MutableStateFlow(0f) // 0 means all, otherwise 100f, 200f, 500f
    val radioSeleccionado: StateFlow<Float> = _radioSeleccionado.asStateFlow()

    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> = combine(_lugares, _categoriaSeleccionada, _radioSeleccionado) { lugares, cat, radio ->
        var lista = if (cat == "Todos") lugares
        else lugares.filter { it.lugar.categoria.equals(cat, ignoreCase = true) }

        if (radio > 0f) {
            lista = lista.filter { it.metros != null && it.metros <= radio }
        }
        lista
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    private val _masCercano = MutableStateFlow<Lugar?>(null)
    val masCercano: StateFlow<Lugar?> = _masCercano.asStateFlow()

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    fun seleccionarRadio(radio: Float) {
        _radioSeleccionado.value = radio
    }

    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            _masCercano.value = null
            return
        }
        _estado.value = UbicacionUi.Buscando
        _masCercano.value = null
        viewModelScope.launch {
            try {
                ubicacionRepo.obtenerPosicion()
                    .onSuccess { posicion ->
                        _estado.value = UbicacionUi.Lista(posicion)
                        actualizarDistancias(posicion)
                    }
                    .onFailure { e ->
                        val tipo = if (e is SecurityException || e.message?.contains("permiso", true) == true) {
                            TipoError.SIN_PERMISO
                        } else {
                            TipoError.GPS_APAGADO
                        }
                        _estado.value = UbicacionUi.Error(
                            e.message ?: "Activa el GPS e intenta de nuevo",
                            tipo
                        )
                    }
            } catch (e: CancellationException) { throw e }
              catch (e: Exception) {
                  _estado.value = UbicacionUi.Error(
                      e.message ?: "Activa el GPS e intenta de nuevo",
                      TipoError.GPS_APAGADO
                  )
              }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
        _masCercano.value = null
    }

    fun actualizarDistancias(pos: Posicion) {
        val listaConDistancia = lugaresRepo.obtenerLugares().map { lugar ->
            val metros = distanciaMetros(pos, lugar)
            LugarConDistancia(lugar, metros)
        }.sortedBy { it.metros ?: Float.MAX_VALUE }
        _lugares.value = listaConDistancia
        _masCercano.value = listaConDistancia.firstOrNull()?.lugar
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }
}
