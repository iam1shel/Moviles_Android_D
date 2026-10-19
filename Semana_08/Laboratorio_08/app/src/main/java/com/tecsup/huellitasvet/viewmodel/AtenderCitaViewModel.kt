package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtenderCitaViewModel(
    private val repo: VetRepository,
    citaId: Int
) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errorDiagnostico = MutableStateFlow<String?>(null)
    val errorDiagnostico: StateFlow<String?> = _errorDiagnostico
    private val _errorPeso = MutableStateFlow<String?>(null)
    val errorPeso: StateFlow<String?> = _errorPeso
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje
    private val _listo = MutableStateFlow(false)
    val listo: StateFlow<Boolean> = _listo

    fun registrar(diagnostico: String, tratamiento: String, peso: Double) {
        val actual = cita.value ?: return
        val estado = actual.cita.estado
        if (estado != EstadoCita.PENDIENTE.name && estado != EstadoCita.CONFIRMADA.name) {
            _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
            return
        }
        var ok = true
        if (diagnostico.trim().length < 5) {
            _errorDiagnostico.value = "El diagnóstico debe tener al menos 5 caracteres"
            ok = false
        } else _errorDiagnostico.value = null
        if (peso <= 0) {
            _errorPeso.value = "El peso debe ser mayor que 0"
            ok = false
        } else _errorPeso.value = null
        if (!ok) return
        viewModelScope.launch {
            repo.registrarAtencion(actual.cita.id, diagnostico.trim(), tratamiento.trim(), peso)
            _listo.value = true
        }
    }

    fun cancelar() {
        val actual = cita.value ?: return
        val estado = actual.cita.estado
        if (estado != EstadoCita.PENDIENTE.name && estado != EstadoCita.CONFIRMADA.name) return
        viewModelScope.launch {
            repo.cambiarEstadoCita(actual.cita.id, EstadoCita.CANCELADA)
            _listo.value = true
        }
    }
}
