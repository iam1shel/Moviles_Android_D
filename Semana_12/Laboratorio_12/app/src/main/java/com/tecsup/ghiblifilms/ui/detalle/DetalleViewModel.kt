package com.tecsup.ghiblifilms.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.ghiblifilms.AppContainer
import com.tecsup.ghiblifilms.data.repository.ItemRepository
import com.tecsup.ghiblifilms.domain.model.Item
import com.tecsup.ghiblifilms.ui.UiState
import com.tecsup.ghiblifilms.ui.mensajeDeError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val id: String,
    private val repo: ItemRepository
) : ViewModel() {

    private val detalle = MutableStateFlow<UiState<Item>>(UiState.Cargando)

    val estado: StateFlow<UiState<Item>> = combine(detalle, repo.favoritos) { actual, favoritos ->
        if (actual is UiState.Exito) {
            UiState.Exito(actual.datos.copy(esFavorito = favoritos.any { it.id == id }))
        } else {
            actual
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            detalle.value = UiState.Cargando
            repo.obtenerDetalle(id)
                .onSuccess { detalle.value = UiState.Exito(it) }
                .onFailure { detalle.value = UiState.Error(mensajeDeError(it)) }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }

    companion object {
        const val ARG_ID = "id"

        fun factory(container: AppContainer) = viewModelFactory {
            initializer {
                val id: String = checkNotNull(createSavedStateHandle()[ARG_ID])
                DetalleViewModel(id, container.itemRepository)
            }
        }
    }
}
