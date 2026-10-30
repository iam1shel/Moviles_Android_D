package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository

class VetViewModelFactory(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore,
    private val duenoId: Int = 0,
    private val mascotaId: Int = 0,
    private val citaId: Int = 0
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val vm = when (modelClass) {
            SesionViewModel::class.java -> SesionViewModel(repo, prefs)
            ServiciosViewModel::class.java -> ServiciosViewModel(repo)
            InicioViewModel::class.java -> InicioViewModel(repo, duenoId)
            MascotaViewModel::class.java -> MascotaViewModel(repo, mascotaId)
            PedirCitaViewModel::class.java -> PedirCitaViewModel(repo, prefs, duenoId, mascotaId)
            MisCitasViewModel::class.java -> MisCitasViewModel(repo, duenoId)
            PanelViewModel::class.java -> PanelViewModel(repo)
            AgendaViewModel::class.java -> AgendaViewModel(repo)
            AtenderCitaViewModel::class.java -> AtenderCitaViewModel(repo, citaId)
            PacientesViewModel::class.java -> PacientesViewModel(repo)
            AdministracionViewModel::class.java -> AdministracionViewModel(repo)
            else -> error("ViewModel no soportado: ${modelClass.name}")
        }
        return vm as T
    }
}
