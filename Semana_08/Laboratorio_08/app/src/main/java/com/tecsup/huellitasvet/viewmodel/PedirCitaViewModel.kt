package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(
    private val repo: VetRepository,
    prefs: PreferenciasStore,
    private val duenoId: Int,
    mascotaIdInicial: Int
) : ViewModel() {

    private var guardando = false

    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )

    val mascotaId = MutableStateFlow(mascotaIdInicial)
    val servicioId = MutableStateFlow(0)
    val fecha = MutableStateFlow(LocalDate.now().toString())
    val hora = MutableStateFlow("")
    val nota = MutableStateFlow("")

    val horasOcupadas: StateFlow<List<String>> = fecha
        .flatMapLatest { repo.observarHorasOcupadas(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje
    private val _creada = MutableStateFlow(false)
    val creada: StateFlow<Boolean> = _creada

    fun seleccionarFecha(valor: String) {
        fecha.value = valor
        hora.value = ""
    }

    fun horaPasada(horaTxt: String, fechaTxt: String = fecha.value): Boolean {
        if (fechaTxt != LocalDate.now().toString()) return false
        return try {
            LocalTime.parse(horaTxt) <= LocalTime.now()
        } catch (_: Exception) {
            false
        }
    }

    fun confirmar() {
        if (guardando) return
        guardando = true
        viewModelScope.launch {
            try {
                validarYCrear()
            } finally {
                guardando = false
            }
        }
    }

    private suspend fun validarYCrear() {
        if (!aceptarCitas.value) {
            _mensaje.value = "La clínica no está recibiendo citas"
            return
        }
        val m = mascotaId.value
        val s = servicioId.value
        val f = fecha.value
        val h = hora.value
        when {
            m == 0 -> { _mensaje.value = "Elige una mascota"; return }
            s == 0 -> { _mensaje.value = "Elige un servicio"; return }
            f.isBlank() -> { _mensaje.value = "Elige una fecha"; return }
            h.isBlank() -> { _mensaje.value = "Elige una hora"; return }
        }
        val dia = runCatching { LocalDate.parse(f) }.getOrNull()
        val hoy = LocalDate.now()
        if (dia == null || dia.isBefore(hoy) || dia.isAfter(hoy.plusDays(7))) {
            _mensaje.value = "La fecha debe estar entre hoy y los próximos 7 días"
            return
        }
        if (h !in Horarios.todos) {
            _mensaje.value = "Horario no permitido (09:00 a 17:30, cada 30 min)"
            hora.value = ""
            return
        }
        if (horaPasada(h, f)) {
            _mensaje.value = "Esa hora ya pasó"
            hora.value = ""
            return
        }
        if (repo.observarServiciosActivos().first().none { it.id == s }) {
            _mensaje.value = "Ese servicio ya no está disponible"
            servicioId.value = 0
            return
        }
        if (repo.observarMascotasDeDueno(duenoId).first().none { it.id == m }) {
            _mensaje.value = "La mascota no pertenece a tu cuenta"
            mascotaId.value = 0
            return
        }
        if (repo.horarioOcupado(f, h)) {
            _mensaje.value = "Ese horario acaba de ocuparse. Elige otro"
            hora.value = ""
            return
        }
        val id = repo.crearCita(m, s, f, h, nota.value)
        if (id > 0) _creada.value = true else _mensaje.value = "No se pudo crear la cita"
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
