package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.model.Usuario

data class MedicosPorEspecialidad(
    val especialidad: Especialidad,
    val medicos: List<Medico>
)

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
        // Cardiología
        Medico(1, "Dra. Ana Ruiz", 1, 4.8, 12),
        Medico(2, "Dr. Luis Soto", 1, 4.5, 8),
        Medico(6, "Dr. Marco Quispe", 1, 4.7, 10),
        // Pediatría
        Medico(3, "Dra. Carmen Díaz", 2, 4.9, 15),
        Medico(7, "Dr. Jorge Ramos", 2, 4.4, 9),
        Medico(8, "Dra. Sofía Mendoza", 2, 4.6, 11),
        // Dermatología
        Medico(4, "Dr. Pedro Vega", 3, 4.2, 6),
        Medico(9, "Dra. Lucía Torres", 3, 4.8, 14),
        Medico(10, "Dr. Andrés Flores", 3, 4.3, 7),
        // Otras
        Medico(5, "Dra. Elena Paz", 4, 4.6, 10),
        Medico(11, "Dr. Renato Salas", 5, 4.5, 9)
    )
    private val sedes = listOf(
        Sede(1, "La Molina"),
        Sede(2, "Miraflores"),
        Sede(3, "San Isidro"),
        Sede(4, "Barranco"),
        Sede(5, "Magdalena del Mar"),
        Sede(6, "San Borja")
    )
    private val citas = mutableListOf<Cita>()
    private val horariosBase = listOf(
        "09:00", "09:30", "10:00", "10:30", "11:00",
        "11:30", "15:00", "15:30", "16:00", "16:30"
    )
    private var siguienteCitaId = 1

    var usuarioActual: Usuario? = null
        private set

    fun registrarUsuario(nombre: String, correo: String, telefono: String, clave: String): Boolean {
        if (usuarios.any { it.correo.equals(correo, ignoreCase = true) }) return false
        val id = (usuarios.maxOfOrNull { it.id } ?: 0) + 1
        val nuevo = Usuario(id, nombre, correo, telefono, clave)
        usuarios.add(nuevo)
        usuarioActual = nuevo
        return true
    }

    fun iniciarSesion(correo: String, clave: String): Boolean {
        val encontrado = usuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.clave == clave
        }
        usuarioActual = encontrado
        return encontrado != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(query: String): List<Especialidad> {
        if (query.isBlank()) return especialidades.toList()
        return especialidades.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                it.descripcion.contains(query, ignoreCase = true)
        }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.filter { it.destacada }.take(3)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? = especialidades.find { it.id == id }

    fun obtenerMedico(id: Int): Medico? = medicos.find { it.id == id }

    fun obtenerCita(id: Int): Cita? = citas.find { it.id == id }

    fun listarSedes(): List<Sede> = sedes

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.rating }
    }

    fun medicosAgrupadosPorEspecialidad(): List<MedicosPorEspecialidad> {
        return especialidades.mapNotNull { esp ->
            val lista = medicosPorEspecialidad(esp.id)
            if (lista.isEmpty()) null else MedicosPorEspecialidad(esp, lista)
        }
    }

    fun buscarMedicos(especialidadId: Int, query: String): List<Medico> {
        val base = medicosPorEspecialidad(especialidadId)
        if (query.isBlank()) return base
        return base.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
            .toSet()
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(
        usuarioId: Int,
        medicoId: Int,
        especialidadId: Int,
        fecha: String,
        hora: String
    ): Cita? {
        val yaExiste = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora
        }
        if (yaExiste) return null
        val cita = Cita(
            id = siguienteCitaId++,
            usuarioId = usuarioId,
            medicoId = medicoId,
            especialidadId = especialidadId,
            fecha = fecha,
            hora = hora
        )
        citas.add(cita)
        return cita
    }

    fun citasDelUsuario(usuarioId: Int): List<Cita> {
        return citas
            .filter { it.usuarioId == usuarioId }
            .sortedWith(compareBy({ it.fecha }, { it.hora }))
    }

    fun cancelarCita(citaId: Int): Boolean {
        return citas.removeIf { it.id == citaId }
    }

    /** Días fijos para Fase 1 (calendario dinámico en con-ia). */
    fun diasFijosFase1(): List<String> = listOf(
        "2026-10-13",
        "2026-10-14",
        "2026-10-15",
        "2026-10-16",
        "2026-10-17"
    )
}
