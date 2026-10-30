package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AdministracionViewModel(repo: VetRepository) : ViewModel() {
    val mascotas: StateFlow<List<MascotaConDueno>> = repo.observarTodasLasMascotas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val duenos: StateFlow<List<Usuario>> = repo.observarUsuariosPorRol(Rol.CLIENTE.name)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
