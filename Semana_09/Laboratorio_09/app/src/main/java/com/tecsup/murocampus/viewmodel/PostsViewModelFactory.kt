package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// La Activity proporcionará el constructor del ViewModel [A] cuando esté disponible.
class PostsViewModelFactory(
    private val tipo: Class<out ViewModel>,
    private val crearViewModel: () -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == tipo) { "ViewModel no disponible: ${modelClass.name}" }
        return requireNotNull(modelClass.cast(crearViewModel()))
    }
}
