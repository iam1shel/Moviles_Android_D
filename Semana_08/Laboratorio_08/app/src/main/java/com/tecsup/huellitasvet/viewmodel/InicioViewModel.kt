package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class InicioViewModel(
    repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val proximaCita: StateFlow<CitaConDetalle?> = repo.observarCitasDeDueno(duenoId)
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista.filter { detalle ->
                val estado = detalle.cita.estado
                val vigente = estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name
                vigente && fechaHora(detalle) >= ahora
            }.minByOrNull { fechaHora(it) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun fechaHora(d: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(d.cita.fecha), LocalTime.parse(d.cita.hora))
}
