package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _errorNombre = MutableStateFlow<String?>(null)
    val errorNombre: StateFlow<String?> = _errorNombre
    private val _errorCorreo = MutableStateFlow<String?>(null)
    val errorCorreo: StateFlow<String?> = _errorCorreo
    private val _errorClave = MutableStateFlow<String?>(null)
    val errorClave: StateFlow<String?> = _errorClave
    private val _errorConfirmar = MutableStateFlow<String?>(null)
    val errorConfirmar: StateFlow<String?> = _errorConfirmar

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )
    val notificaciones: StateFlow<Boolean> = prefs.notificaciones.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )
    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )
    val perfilActivo: StateFlow<String> = prefs.perfilActivo.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), ""
    )
    val usuarioId: StateFlow<Int> = prefs.usuarioId.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), 0
    )

    init {
        viewModelScope.launch {
            prefs.sesionGuardada.collect { (perfil, id) ->
                _usuarioActual.value = when {
                    id == 0 -> null
                    perfil == Rol.ADMINISTRADOR.name -> repo.obtenerAdministrador(id)?.comoUsuario()
                    else -> repo.obtenerUsuario(id)
                }
            }
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol, onOk: (Rol) -> Unit) {
        viewModelScope.launch {
            if (correo.isBlank() || clave.isBlank()) {
                _error.value = "Completa correo y contraseña"
                return@launch
            }
            val correoLimpio = correo.trim()
            val usuario = if (rolSeleccionado == Rol.ADMINISTRADOR) {
                repo.loginAdministrador(correoLimpio, clave)?.comoUsuario()
            } else {
                repo.login(correoLimpio, clave)
            }
            val existeEnOtroPerfil = usuario == null && when (rolSeleccionado) {
                Rol.ADMINISTRADOR -> repo.login(correoLimpio, clave) != null
                else -> repo.loginAdministrador(correoLimpio, clave) != null
            }
            when {
                usuario == null && existeEnOtroPerfil ->
                    _error.value = "Este usuario no corresponde al perfil seleccionado"
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Este usuario no corresponde al perfil seleccionado"
                else -> {
                    _error.value = null
                    _usuarioActual.value = usuario
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    onOk(rolSeleccionado)
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun registrar(
        nombre: String,
        correo: String,
        clave: String,
        confirmar: String,
        onOk: (correo: String, clave: String) -> Unit
    ) {
        viewModelScope.launch {
            val nombreLimpio = nombre.trim()
            val correoLimpio = correo.trim()
            var ok = true
            if (nombreLimpio.length < 2) {
                _errorNombre.value = "El nombre debe tener al menos 2 caracteres"
                ok = false
            } else _errorNombre.value = null
            val correoValido = correoLimpio.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            if (!correoValido) {
                _errorCorreo.value = "Ingresa un correo válido"
                ok = false
            } else if (
                repo.buscarPorCorreo(correoLimpio) != null ||
                repo.buscarAdministradorPorCorreo(correoLimpio) != null
            ) {
                _errorCorreo.value = "Este correo ya está registrado"
                ok = false
            } else _errorCorreo.value = null
            if (clave.length < 4) {
                _errorClave.value = "Mínimo 4 caracteres"
                ok = false
            } else _errorClave.value = null
            if (clave != confirmar) {
                _errorConfirmar.value = "Las contraseñas no coinciden"
                ok = false
            } else _errorConfirmar.value = null
            if (!ok) return@launch
            repo.registrar(
                Usuario(
                    nombre = nombreLimpio,
                    correo = correoLimpio,
                    clave = clave,
                    rol = Rol.CLIENTE.name
                )
            )
            onOk(correoLimpio, clave)
        }
    }

    fun guardarModoOscuro(b: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(b) }
    }

    fun guardarNotificaciones(b: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(b) }
    }

    fun guardarAceptarCitas(b: Boolean) {
        viewModelScope.launch { prefs.guardarAceptarCitas(b) }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch { prefs.cerrarSesion() }
    }

    fun cambiarPerfil(onListo: (Rol) -> Unit) {
        viewModelScope.launch {
            val actual = _usuarioActual.value ?: return@launch
            val otroRol = if (actual.rol == Rol.CLIENTE.name) Rol.ADMIN else Rol.CLIENTE
            val otro = repo.obtenerUsuarioPorRol(otroRol.name) ?: return@launch
            prefs.guardarSesion(otro.rol, otro.id)
            onListo(otroRol)
        }
    }
}
