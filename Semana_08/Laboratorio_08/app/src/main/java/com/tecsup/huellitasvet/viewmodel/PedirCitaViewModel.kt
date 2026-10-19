package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(
    private val repo: VetRepository,
    prefs: PreferenciasStore,
    duenoId: Int,
    mascotaIdInicial: Int
) : ViewModel() {

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
        viewModelScope.launch {
            if (!aceptarCitas.value) {
                _mensaje.value = "La clínica no está recibiendo citas"
                return@launch
            }
            val m = mascotaId.value
            val s = servicioId.value
            val f = fecha.value
            val h = hora.value
            if (m == 0 || s == 0 || f.isBlank() || h.isBlank()) {
                _mensaje.value = "Completa mascota, servicio, fecha y hora"
                return@launch
            }
            if (repo.horarioOcupado(f, h)) {
                _mensaje.value = "Ese horario ya está ocupado"
                return@launch
            }
            val id = repo.crearCita(m, s, f, h, nota.value)
            if (id > 0) _creada.value = true
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
