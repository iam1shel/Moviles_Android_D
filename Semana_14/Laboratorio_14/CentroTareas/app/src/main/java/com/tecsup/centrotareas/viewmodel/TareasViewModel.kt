package com.tecsup.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _resultadoUnaPorUna = MutableStateFlow<CargaUi.Exito?>(null)
    val resultadoUnaPorUna: StateFlow<CargaUi.Exito?> = _resultadoUnaPorUna.asStateFlow()

    private val _resultadoALaVez = MutableStateFlow<CargaUi.Exito?>(null)
    val resultadoALaVez: StateFlow<CargaUi.Exito?> = _resultadoALaVez.asStateFlow()

    private val _tiempoUnaPorUna = MutableStateFlow<Long?>(null)
    val tiempoUnaPorUna: StateFlow<Long?> = _tiempoUnaPorUna.asStateFlow()

    private val _tiempoALaVez = MutableStateFlow<Long?>(null)
    val tiempoALaVez: StateFlow<Long?> = _tiempoALaVez.asStateFlow()

    private val _cargandoModo = MutableStateFlow<String?>(null)
    val cargandoModo: StateFlow<String?> = _cargandoModo.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    private suspend fun <T> ejecutarConReintentos(modo: String, block: suspend () -> T): T {
        val maxReintentos = 3
        var intento = 0
        while (true) {
            try {
                if (intento == 0) {
                    _carga.value = CargaUi.Cargando("Intentando $modo...")
                } else {
                    _carga.value = CargaUi.Cargando("Reintentando $modo ($intento de 3)...")
                }
                return block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (intento >= maxReintentos) {
                    throw e
                }
                intento++
                delay(1000L * intento)
            }
        }
    }

    fun cargarUnaPorUna() {

        if (_cargandoModo.value != null) return

        _cargandoModo.value = "Una por una"

        viewModelScope.launch {

            val inicio = System.currentTimeMillis()

            try {

                val resultado = ejecutarConReintentos("Una por una") {
                    val perfil = repo.cargarPerfil()
                    val pedidos = repo.cargarPedidos(_simularError.value)
                    Pair(perfil, pedidos)
                }

                val tiempo = System.currentTimeMillis() - inicio
                _tiempoUnaPorUna.value = tiempo

                val exito = CargaUi.Exito(
                    modo = "Una por una",
                    perfil = resultado.first,
                    pedidos = resultado.second.size,
                    milisegundos = tiempo
                )

                _resultadoUnaPorUna.value = exito
                _carga.value = exito

            } catch (e: CancellationException) {
                _carga.value = CargaUi.Error("Cancelado")
                throw e

            } catch (e: Exception) {

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: ${e.message}"
                )
            } finally {
                _cargandoModo.value = null
            }
        }
    }

    fun cargarALaVez() {

        if (_cargandoModo.value != null) return

        _cargandoModo.value = "A la vez"

        viewModelScope.launch {

            val inicio = System.currentTimeMillis()

            try {

                val resultado = ejecutarConReintentos("A la vez") {
                    coroutineScope {

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
                }

                val tiempo = System.currentTimeMillis() - inicio
                _tiempoALaVez.value = tiempo

                val exito = CargaUi.Exito(
                    modo = "A la vez",
                    perfil = resultado.first,
                    pedidos = resultado.second.size,
                    milisegundos = tiempo
                )

                _resultadoALaVez.value = exito
                _carga.value = exito

            } catch (e: CancellationException) {
                _carga.value = CargaUi.Error("Cancelado")
                throw e

            } catch (e: Exception) {

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: ${e.message}"
                )
            } finally {
                _cargandoModo.value = null
            }
        }
    }
}
