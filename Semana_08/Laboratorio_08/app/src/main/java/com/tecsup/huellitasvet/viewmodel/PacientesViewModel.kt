package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PacientesViewModel(repo: VetRepository) : ViewModel() {
    val consulta = MutableStateFlow("")
    val especie = MutableStateFlow<Especie?>(null)

    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(),
        consulta,
        especie
    ) { lista, texto, filtro ->
        val q = texto.trim().lowercase()
        lista.filter { item ->
            val coincideTexto = q.isEmpty() ||
                item.mascota.nombre.lowercase().contains(q) ||
                item.dueno.nombre.lowercase().contains(q)
            val coincideEspecie = filtro == null || item.mascota.especie == filtro.name
            coincideTexto && coincideEspecie
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val ultimasVisitas: StateFlow<Map<Int, LocalDate>> = repo.observarUltimasVisitas()
        .map { lista ->
            lista.mapNotNull { v ->
                runCatching { LocalDate.parse(v.fecha) }.getOrNull()?.let { v.mascotaId to it }
            }.toMap()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}
