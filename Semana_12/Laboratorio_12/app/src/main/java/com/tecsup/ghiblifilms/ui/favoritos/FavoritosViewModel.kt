package com.tecsup.ghiblifilms.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.ghiblifilms.AppContainer
import com.tecsup.ghiblifilms.data.repository.ItemRepository
import com.tecsup.ghiblifilms.domain.model.Item
import com.tecsup.ghiblifilms.ui.UiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repo: ItemRepository) : ViewModel() {

    val estado: StateFlow<UiState<List<Item>>> = repo.favoritos
        .map<List<Item>, UiState<List<Item>>> { UiState.Exito(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)

    fun alternarFavorito(id: String) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer { FavoritosViewModel(container.itemRepository) }
        }
    }
}
