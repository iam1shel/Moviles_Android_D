package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.UsuarioRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repo: UsuarioRepository) : ViewModel() {
    private val _usuarios = MutableStateFlow<UiState<List<Usuario>>>(UiState.Cargando)
    val usuarios = _usuarios.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _usuarios.value = UiState.Cargando
            _usuarios.value = repo.listar().fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(mensajeDe(it)) }
            )
        }
    }
}
