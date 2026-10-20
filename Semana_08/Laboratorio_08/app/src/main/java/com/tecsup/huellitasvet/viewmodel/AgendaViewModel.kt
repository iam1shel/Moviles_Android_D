package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {
    val fechaSeleccionada = MutableStateFlow(LocalDate.now().toString())

    val citas: StateFlow<List<CitaConDetalle>> = fechaSeleccionada
        .flatMapLatest { repo.observarCitasPorFecha(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun confirmar(cita: CitaConDetalle) {
        if (cita.cita.estado != EstadoCita.PENDIENTE.name) return
        viewModelScope.launch { repo.cambiarEstadoCita(cita.cita.id, EstadoCita.CONFIRMADA) }
    }

    fun cancelar(cita: CitaConDetalle) {
        val estado = cita.cita.estado
        if (estado != EstadoCita.PENDIENTE.name && estado != EstadoCita.CONFIRMADA.name) return
        viewModelScope.launch { repo.cambiarEstadoCita(cita.cita.id, EstadoCita.CANCELADA) }
    }
}
