package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.PuntoPeso
import com.tecsup.huellitasvet.model.Razas
import com.tecsup.huellitasvet.model.Sexo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class MascotaViewModel(
    private val repo: VetRepository,
    mascotaId: Int
) : ViewModel() {

    val mascota: StateFlow<Mascota?> = repo.observarMascota(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val historial: StateFlow<List<CitaConDetalle>> = repo.observarHistorial(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pesoHistorial: StateFlow<List<PuntoPeso>> = repo.observarPesoHistorial(mascotaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errorNombre = MutableStateFlow<String?>(null)
    val errorNombre: StateFlow<String?> = _errorNombre
    private val _errorEspecie = MutableStateFlow<String?>(null)
    val errorEspecie: StateFlow<String?> = _errorEspecie
    private val _errorSexo = MutableStateFlow<String?>(null)
    val errorSexo: StateFlow<String?> = _errorSexo
    private val _errorAnio = MutableStateFlow<String?>(null)
    val errorAnio: StateFlow<String?> = _errorAnio
    private val _errorPeso = MutableStateFlow<String?>(null)
    val errorPeso: StateFlow<String?> = _errorPeso
    private val _errorRaza = MutableStateFlow<String?>(null)
    val errorRaza: StateFlow<String?> = _errorRaza
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje
    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> = _guardado

    fun guardar(mascota: Mascota, exigirRazaDeCatalogo: Boolean = false) {
        val anioActual = LocalDate.now().year
        var ok = true
        if (mascota.nombre.trim().length < 2) {
            _errorNombre.value = "El nombre debe tener al menos 2 caracteres"
            ok = false
        } else _errorNombre.value = null
        if (mascota.especie !in Especie.entries.map { it.name }) {
            _errorEspecie.value = "Elige una especie"
            ok = false
        } else _errorEspecie.value = null
        if (mascota.sexo !in Sexo.entries.map { it.name }) {
            _errorSexo.value = "Elige el sexo"
            ok = false
        } else _errorSexo.value = null
        if (mascota.anioNacimiento !in 1990..anioActual) {
            _errorAnio.value = "Año entre 1990 y $anioActual"
            ok = false
        } else _errorAnio.value = null
        if (mascota.peso <= 0) {
            _errorPeso.value = "El peso debe ser mayor que 0"
            ok = false
        } else _errorPeso.value = null
        val razaDeLista = mascota.raza in Razas.de(mascota.especie)
        if (exigirRazaDeCatalogo && mascota.raza.isBlank()) {
            _errorRaza.value = "Elige una raza"
            ok = false
        } else if (exigirRazaDeCatalogo && mascota.id == 0 && !razaDeLista) {
            _errorRaza.value = "Elige una raza de la lista"
            ok = false
        } else _errorRaza.value = null
        if (!ok) return
        viewModelScope.launch {
            repo.guardarMascota(mascota)
            _guardado.value = true
        }
    }

    fun eliminar(mascota: Mascota, onOk: () -> Unit) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(mascota.id)) {
                _mensaje.value = "No se puede eliminar una mascota con citas"
            } else {
                repo.eliminarMascota(mascota)
                onOk()
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun consumirGuardado() {
        _guardado.value = false
    }
}
