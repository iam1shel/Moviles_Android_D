package com.tecsup.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TareasViewModel(
    private val repo: TareasRepository
) : ViewModel() {

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private var trabajo: Job? = null

    fun tareaLarga() {

        if (_corriendo.value) return

        _corriendo.value = true
        _progreso.value = 0f
        _mensaje.value = "Trabajando..."

        trabajo = viewModelScope.launch {

            try {

                Log.d(
                    "HILO",
                    "Antes: ${Thread.currentThread().name}"
                )

                repo.observarProgreso().collect { valor ->
                    _progreso.value = valor
                }

                Log.d(
                    "HILO",
                    "Después: ${Thread.currentThread().name}"
                )

                _mensaje.value = "Terminó"

            } catch (e: CancellationException) {

                _mensaje.value = "Cancelado"
                throw e

            } finally {

                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        trabajo?.cancel()
    }

    private val _primos = MutableStateFlow<PrimosUi>(
        PrimosUi.Inicial
    )

    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {

        if (hasta < 2) return

        if (trabajoPrimos?.isActive == true) return

        trabajoPrimos = viewModelScope.launch {

            _primos.value = PrimosUi.Calculando

            val inicio = System.currentTimeMillis()

            try {

                val cantidad = repo.contarPrimos(hasta)

                val tiempo = System.currentTimeMillis() - inicio

                _primos.value = PrimosUi.Listo(
                    cantidad = cantidad,
                    milisegundos = tiempo
                )

            } catch (e: CancellationException) {

                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    private val _carga = MutableStateFlow<CargaUi>(
        CargaUi.Inicial
    )

    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    fun cargarUnaPorUna() {

        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {

            val inicio = System.currentTimeMillis()

            try {

                val perfil = repo.cargarPerfil()

                val pedidos = repo.cargarPedidos(_simularError.value)

                val tiempo = System.currentTimeMillis() - inicio

                _carga.value = CargaUi.Exito(
                    modo = "Una por una",
                    perfil = perfil,
                    pedidos = pedidos.size,
                    milisegundos = tiempo
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: ${e.message}"
                )
            }
        }
    }

    fun cargarALaVez() {

        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {

            val inicio = System.currentTimeMillis()

            try {

                val resultado = coroutineScope {

                    val perfil = async {
                        repo.cargarPerfil()
                    }

                    val pedidos = async {
                        repo.cargarPedidos(_simularError.value)
                    }

                    Pair(
                        perfil.await(),
                        pedidos.await()
                    )
                }

                val tiempo = System.currentTimeMillis() - inicio

                _carga.value = CargaUi.Exito(
                    modo = "A la vez",
                    perfil = resultado.first,
                    pedidos = resultado.second.size,
                    milisegundos = tiempo
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: ${e.message}"
                )
            }
        }
    }
}
