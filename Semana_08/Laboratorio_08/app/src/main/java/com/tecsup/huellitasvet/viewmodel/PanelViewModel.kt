package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.DatoGrafico
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.theme.AtendidaColor
import com.tecsup.huellitasvet.ui.theme.AzulVet
import com.tecsup.huellitasvet.ui.theme.CanceladaColor
import com.tecsup.huellitasvet.ui.theme.ConfirmadaColor
import com.tecsup.huellitasvet.ui.theme.PendienteColor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelViewModel(repo: VetRepository) : ViewModel() {
    private val hoy = LocalDate.now().toString()
    private val inicioMes = LocalDate.now().withDayOfMonth(1).toString()

    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val pendientes: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val atendidas: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(inicioMes)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoCita.PENDIENTE),
        repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA),
        repo.observarCantidadPorEstado(EstadoCita.ATENDIDA),
        repo.observarCantidadPorEstado(EstadoCita.CANCELADA)
    ) { p, c, a, x ->
        listOf(
            DatoGrafico("Pendiente", p.toFloat(), PendienteColor),
            DatoGrafico("Confirmada", c.toFloat(), ConfirmadaColor),
            DatoGrafico("Atendida", a.toFloat(), AtendidaColor),
            DatoGrafico("Cancelada", x.toFloat(), CanceladaColor)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val coloresServicio = listOf(AzulVet, PendienteColor, ConfirmadaColor, AtendidaColor)

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarCitasPorServicio()
        .map { lista ->
            lista.mapIndexed { i, item ->
                DatoGrafico(item.nombre, item.cantidad.toFloat(), coloresServicio[i % coloresServicio.size])
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
