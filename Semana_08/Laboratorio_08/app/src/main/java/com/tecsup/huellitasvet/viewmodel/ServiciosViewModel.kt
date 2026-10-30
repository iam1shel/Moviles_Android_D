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

    val duracionesPermitidas = listOf(15, 30, 45, 60)

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje
    private val _errorNombre = MutableStateFlow<String?>(null)
    val errorNombre: StateFlow<String?> = _errorNombre
    private val _errorDuracion = MutableStateFlow<String?>(null)
    val errorDuracion: StateFlow<String?> = _errorDuracion
    private val _errorPrecio = MutableStateFlow<String?>(null)
    val errorPrecio: StateFlow<String?> = _errorPrecio

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    fun guardar(
        id: Int,
        nombre: String,
        descripcion: String,
        duracionMin: Int,
        precioTxt: String,
        activo: Boolean,
        icono: String,
        onOk: () -> Unit
    ) {
        var ok = true
        if (nombre.isBlank()) {
            _errorNombre.value = "El nombre es obligatorio"
            ok = false
        } else _errorNombre.value = null
        if (duracionMin !in duracionesPermitidas) {
            _errorDuracion.value = "La duración debe ser 15, 30, 45 o 60 min"
            ok = false
        } else _errorDuracion.value = null
        val precio = precioTxt.trim().replace(",", ".").toDoubleOrNull()
        if (precio == null || precio <= 0) {
            _errorPrecio.value = "El precio debe ser mayor que 0"
            ok = false
        } else _errorPrecio.value = null
        if (!ok || precio == null) return
        viewModelScope.launch {
            repo.guardarServicio(
                Servicio(
                    id = id,
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    duracionMin = duracionMin,
                    precio = precio,
                    activo = activo,
                    icono = icono
                )
            )
            onOk()
        }
    }

    fun eliminar(servicio: Servicio, onOk: () -> Unit) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(servicio.id)) {
                _mensaje.value = "No se puede eliminar un servicio con citas. Solo se desactiva."
            } else {
                repo.eliminarServicio(servicio)
                onOk()
            }
        }
    }

    fun limpiarErrores() {
        _errorNombre.value = null
        _errorDuracion.value = null
        _errorPrecio.value = null
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
