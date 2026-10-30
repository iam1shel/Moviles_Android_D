package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
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

    private val _porReprogramar = MutableStateFlow<CitaConDetalle?>(null)
    val porReprogramar: StateFlow<CitaConDetalle?> = _porReprogramar
    val fechaReprogramar = MutableStateFlow(LocalDate.now().toString())
    val horaReprogramar = MutableStateFlow("")
    private val _errorReprogramar = MutableStateFlow<String?>(null)
    val errorReprogramar: StateFlow<String?> = _errorReprogramar

    val horasOcupadasReprogramar: StateFlow<List<String>> = combine(
        fechaReprogramar.flatMapLatest { f -> repo.observarHorasOcupadas(f).map { f to it } },
        _porReprogramar
    ) { (fecha, ocupadas), cita ->
        if (cita != null && cita.cita.fecha == fecha) ocupadas - cita.cita.hora else ocupadas
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun fechasDisponibles(): List<LocalDate> = (0L..7L).map { LocalDate.now().plusDays(it) }

    fun puedeCancelar(d: CitaConDetalle): Boolean =
        esVigente(d.cita.estado) && faltanMasDeDosHoras(d.cita.fecha, d.cita.hora)

    fun cancelar(citaId: Int, fecha: String, hora: String, estado: String) {
        viewModelScope.launch {
            if (!esVigente(estado) || !faltanMasDeDosHoras(fecha, hora)) {
                _mensaje.value = "Ya no puedes cancelar esta cita"
                return@launch
            }
            repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
        }
    }

    fun abrirReprogramar(d: CitaConDetalle) {
        if (!puedeCancelar(d)) {
            _mensaje.value = "Ya no puedes reprogramar esta cita"
            return
        }
        _errorReprogramar.value = null
        _porReprogramar.value = d
        val dia = runCatching { LocalDate.parse(d.cita.fecha) }.getOrNull()
        val fechaValida = dia != null && dia in fechasDisponibles()
        fechaReprogramar.value = if (fechaValida) d.cita.fecha else LocalDate.now().toString()
        horaReprogramar.value = if (fechaValida && !horaPasada(d.cita.hora, d.cita.fecha)) d.cita.hora else ""
    }

    fun cerrarReprogramar() {
        _porReprogramar.value = null
        _errorReprogramar.value = null
    }

    fun seleccionarFechaReprogramar(valor: String) {
        fechaReprogramar.value = valor
        _errorReprogramar.value = null
        val h = horaReprogramar.value
        if (h.isBlank()) return
        if (horaPasada(h, valor)) {
            horaReprogramar.value = ""
            return
        }
        val propia = _porReprogramar.value?.cita
        viewModelScope.launch {
            val esPropia = propia != null && propia.fecha == valor && propia.hora == h
            val ocupada = !esPropia && repo.horarioOcupado(valor, h)
            if (ocupada && fechaReprogramar.value == valor && horaReprogramar.value == h) {
                horaReprogramar.value = ""
            }
        }
    }

    fun seleccionarHoraReprogramar(valor: String) {
        if (horaNoDisponible(valor)) return
        _errorReprogramar.value = null
        horaReprogramar.value = valor
    }

    fun horaNoDisponible(h: String): Boolean =
        h in horasOcupadasReprogramar.value || horaPasada(h, fechaReprogramar.value)

    fun horaPasada(horaTxt: String, fechaTxt: String = fechaReprogramar.value): Boolean {
        if (fechaTxt != LocalDate.now().toString()) return false
        return try {
            LocalTime.parse(horaTxt) <= LocalTime.now()
        } catch (_: Exception) {
            false
        }
    }

    fun reprogramar() {
        val d = _porReprogramar.value ?: return
        val f = fechaReprogramar.value
        val h = horaReprogramar.value
        viewModelScope.launch {
            if (!puedeCancelar(d)) {
                _porReprogramar.value = null
                _mensaje.value = "Ya no puedes reprogramar esta cita"
                return@launch
            }
            val fechaNueva = runCatching { LocalDate.parse(f) }.getOrNull()
            if (fechaNueva == null || fechaNueva !in fechasDisponibles() || h !in Horarios.todos) {
                _errorReprogramar.value = "Elige una fecha y hora válidas"
                return@launch
            }
            if (horaPasada(h, f)) {
                _errorReprogramar.value = "Esa hora ya pasó"
                horaReprogramar.value = ""
                return@launch
            }
            if (f == d.cita.fecha && h == d.cita.hora) {
                _porReprogramar.value = null
                return@launch
            }
            if (repo.horarioOcupado(f, h)) {
                _errorReprogramar.value = "Ese horario ya está ocupado"
                horaReprogramar.value = ""
                return@launch
            }
            repo.reprogramarCita(d.cita.id, f, h)
            _porReprogramar.value = null
            _reprogramada.value = true
            _mensaje.value = "Cita reprogramada"
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    private fun esVigente(estado: String): Boolean =
        estado == EstadoCita.PENDIENTE.name || estado == EstadoCita.CONFIRMADA.name

    private fun faltanMasDeDosHoras(fecha: String, hora: String): Boolean {
        return try {
            val fechaHora = LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora))
            Duration.between(LocalDateTime.now(), fechaHora) > Duration.ofHours(2)
        } catch (_: Exception) {
            false
        }
    }

    private fun fechaHora(d: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(d.cita.fecha), LocalTime.parse(d.cita.hora))
}
