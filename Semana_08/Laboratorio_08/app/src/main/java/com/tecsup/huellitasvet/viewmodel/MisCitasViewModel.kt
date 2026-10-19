package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.faltanMasDeDosHoras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MisCitasViewModel(
    private val repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    private val todas = repo.observarCitasDeDueno(duenoId)

    val proximas: StateFlow<List<CitaConDetalle>> = todas.map { lista ->
        val ahora = LocalDateTime.now()
        lista.filter {
            val vigente = it.cita.estado == EstadoCita.PENDIENTE.name ||
                it.cita.estado == EstadoCita.CONFIRMADA.name
            vigente && fechaHora(it) >= ahora
        }.sortedBy { fechaHora(it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historial: StateFlow<List<CitaConDetalle>> = todas.map { lista ->
        val ahora = LocalDateTime.now()
        lista.filter {
            val final = it.cita.estado == EstadoCita.ATENDIDA.name ||
                it.cita.estado == EstadoCita.CANCELADA.name
            final || fechaHora(it) < ahora
        }.sortedByDescending { fechaHora(it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje
    private val _reprogramada = MutableStateFlow(false)
    val reprogramada: StateFlow<Boolean> = _reprogramada

    fun cancelar(citaId: Int, fecha: String, hora: String, estado: String) {
        viewModelScope.launch {
            val vigente = estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name
            if (!vigente || !faltanMasDeDosHoras(fecha, hora)) {
                _mensaje.value = "Ya no puedes cancelar esta cita"
                return@launch
            }
            repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
        }
    }

    fun reprogramar(citaId: Int, fecha: String, hora: String, estado: String) {
        viewModelScope.launch {
            val vigente = estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name
            if (!vigente || !faltanMasDeDosHoras(fecha, hora)) {
                _mensaje.value = "Ya no puedes reprogramar esta cita"
                return@launch
            }
            if (repo.horarioOcupado(fecha, hora)) {
                _mensaje.value = "Ese horario ya está ocupado"
                return@launch
            }
            repo.reprogramarCita(citaId, fecha, hora)
            _reprogramada.value = true
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    private fun fechaHora(d: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(d.cita.fecha), LocalTime.parse(d.cita.hora))
}
