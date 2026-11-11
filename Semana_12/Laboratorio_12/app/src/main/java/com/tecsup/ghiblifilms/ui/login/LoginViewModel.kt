package com.tecsup.ghiblifilms.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecsup.ghiblifilms.AppContainer
import com.tecsup.ghiblifilms.data.auth.AuthRepository
import com.tecsup.ghiblifilms.domain.model.Sesion
import com.tecsup.ghiblifilms.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Exito(null) = sin sesión, Exito(sesion) = dentro de la app. */
class LoginViewModel(private val auth: AuthRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    private val _sesionVerificada = MutableStateFlow(false)
    val sesionVerificada: StateFlow<Boolean> = _sesionVerificada.asStateFlow()

    init {
        viewModelScope.launch {
            val sesion = try {
                auth.sesionActual()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                null
            }
            _estado.value = UiState.Exito(sesion)
            _sesionVerificada.value = true
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _estado.value = UiState.Error("Ingresa tu correo y tu contraseña")
            return
        }
        if (_estado.value is UiState.Cargando) return
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            auth.iniciarSesion(correo.trim(), clave)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(it.message ?: "No se pudo iniciar sesión") }
        }
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _estado.value = UiState.Exito(null)
    }

    companion object {
        fun factory(container: AppContainer) = viewModelFactory {
            initializer { LoginViewModel(container.authRepository) }
        }
    }
}
