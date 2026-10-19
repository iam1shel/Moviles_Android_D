package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {
    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    fun guardar(servicio: Servicio) {
        viewModelScope.launch { repo.guardarServicio(servicio) }
    }

    fun eliminar(servicio: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(servicio.id)) {
                _mensaje.value = "No se puede eliminar un servicio con citas. Solo se desactiva."
            } else {
                repo.eliminarServicio(servicio)
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
