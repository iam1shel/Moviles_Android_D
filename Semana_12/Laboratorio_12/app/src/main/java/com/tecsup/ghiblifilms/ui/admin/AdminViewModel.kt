package com.tecsup.ghiblifilms.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.ghiblifilms.AppContainer
import com.tecsup.ghiblifilms.data.repository.ItemRepository
import com.tecsup.ghiblifilms.notification.Notificador
import com.tecsup.ghiblifilms.ui.UiState
import com.tecsup.ghiblifilms.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResumenAdmin(val totalGuardado: Int, val totalFavoritos: Int)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<ResumenAdmin>>(UiState.Cargando)
    val estado: StateFlow<UiState<ResumenAdmin>> = _estado.asStateFlow()

    private val _ocupado = MutableStateFlow(false)
    val ocupado: StateFlow<Boolean> = _ocupado.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        viewModelScope.launch { actualizarTotales() }
    }

    fun sincronizar() = ejecutar {
        repo.refrescar()
            .onSuccess { n ->
                notificador.mostrar("Datos actualizados", "Se guardaron $n películas")
                _mensaje.value = "Sincronización completa: $n películas"
            }
            .onFailure { _mensaje.value = mensajeDeError(it) }
    }

    fun limpiarCache() = ejecutar {
        repo.limpiarCache()
        _mensaje.value = "Se limpió la caché del celular"
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }

    private fun ejecutar(accion: suspend () -> Unit) {
        if (_ocupado.value) return
        _ocupado.value = true
        viewModelScope.launch {
            accion()
            actualizarTotales()
            _ocupado.value = false
        }
    }

    private suspend fun actualizarTotales() {
        _estado.value = UiState.Exito(ResumenAdmin(repo.cantidadGuardada(), repo.cantidadFavoritos()))
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer { AdminViewModel(container.itemRepository, container.notificador) }
        }
    }
}
