package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

/**
 * Colecciones en memoria (sin Room / Firebase).
 * Esqueleto: completar cada TODO en commits posteriores.
 */
object Repositorio {
    private val usuarios = mutableListOf(
        Usuario(1, "Juan Pérez", "juan@correo.com", "999111222", "123456")
    )
    private val especialidades = mutableListOf(
        Especialidad(1, "Cardiología", "Corazón y sistema circulatorio", destacada = true),
        Especialidad(2, "Pediatría", "Salud infantil", destacada = true),
        Especialidad(3, "Dermatología", "Piel y anexos", destacada = true),
        Especialidad(4, "Traumatología", "Huesos y articulaciones"),
        Especialidad(5, "Oftalmología", "Salud visual")
    )
    private val medicos = mutableListOf(
        Medico(1, "Dra. Ana Ruiz", 1, 4.8, 12),
        Medico(2, "Dr. Luis Soto", 1, 4.5, 8),
        Medico(3, "Dra. Carmen Díaz", 2, 4.9, 15),
        Medico(4, "Dr. Pedro Vega", 3, 4.2, 6),
        Medico(5, "Dra. Elena Paz", 4, 4.6, 10)
    )
    private val citas = mutableListOf<Cita>()
    private val horariosBase = listOf(
        "09:00", "09:30", "10:00", "10:30", "11:00",
        "11:30", "15:00", "15:30", "16:00", "16:30"
    )

    var usuarioActual: Usuario? = null
        private set

    // TODO: usuarios: any + add
    fun registrarUsuario(nombre: String, correo: String, telefono: String, clave: String): Boolean {
        return false
    }

    // TODO: usuarios: find; asignar usuarioActual
    fun iniciarSesion(correo: String, clave: String): Boolean {
        return false
    }

    // TODO: limpiar usuarioActual
    fun cerrarSesion() {
    }

    // TODO: especialidades: filter + contains
    fun buscarEspecialidades(query: String): List<Especialidad> {
        return emptyList()
    }

    // TODO: especialidades: take
    fun especialidadesDestacadas(): List<Especialidad> {
        return emptyList()
    }

    fun obtenerEspecialidad(id: Int): Especialidad? = especialidades.find { it.id == id }

    fun obtenerMedico(id: Int): Medico? = medicos.find { it.id == id }

    fun obtenerCita(id: Int): Cita? = citas.find { it.id == id }

    // TODO: medicos: filter + sortedByDescending
    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return emptyList()
    }

    fun buscarMedicos(especialidadId: Int, query: String): List<Medico> {
        return emptyList()
    }

    // TODO: citas filter + horariosBase filter
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        return emptyList()
    }

    // TODO: citas: any + add
    fun agendarCita(
        usuarioId: Int,
        medicoId: Int,
        especialidadId: Int,
        fecha: String,
        hora: String
    ): Cita? {
        return null
    }

    // TODO: citas: filter + sortedWith
    fun citasDelUsuario(usuarioId: Int): List<Cita> {
        return emptyList()
    }

    // TODO reto: removeIf
    fun cancelarCita(citaId: Int): Boolean {
        return false
    }

    /** Acceso interno para implementación (no cambiar firmas públicas). */
    internal fun _usuarios() = usuarios
    internal fun _especialidades() = especialidades
    internal fun _medicos() = medicos
    internal fun _citas() = citas
    internal fun _horariosBase() = horariosBase
    internal fun _setUsuarioActual(u: Usuario?) {
        usuarioActual = u
    }
}
