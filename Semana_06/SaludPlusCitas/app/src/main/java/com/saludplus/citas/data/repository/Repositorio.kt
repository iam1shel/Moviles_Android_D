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
        Especialidad(1, "Medicina General", "Atención integral", destacada = true, colorFondo = 0xFFE3F2FD, colorIcono = 0xFF1E88E5),
        Especialidad(2, "Pediatría", "Niños y adolescentes", destacada = true, colorFondo = 0xFFFFF3E0, colorIcono = 0xFFFB8C00),
        Especialidad(3, "Ginecología", "Salud de la mujer", destacada = true, colorFondo = 0xFFFCE4EC, colorIcono = 0xFFE91E63),
        Especialidad(4, "Cardiología", "Corazón y vasos sanguíneos", colorFondo = 0xFFFFEBEE, colorIcono = 0xFFE53935),
        Especialidad(5, "Dermatología", "Piel, cabello y uñas", colorFondo = 0xFFFFF3E0, colorIcono = 0xFFF57C00),
        Especialidad(6, "Traumatología", "Huesos y articulaciones", colorFondo = 0xFFE3F2FD, colorIcono = 0xFF1565C0),
        Especialidad(7, "Oftalmología", "Salud visual", colorFondo = 0xFFE8EAF6, colorIcono = 0xFF3949AB)
    )
    private val medicos = mutableListOf(
        // Medicina General
        Medico(12, "Dr. Pablo Herrera", 1, 4.6, 10, 98, "Disponible hoy", "11223"),
        Medico(13, "Dra. Valeria Cruz", 1, 4.7, 9, 110, "Disponible mañana", "11224"),
        Medico(14, "Dr. Diego León", 1, 4.5, 8, 87, "Disponible esta semana", "11225"),
        // Pediatría
        Medico(3, "Dra. Carmen Díaz", 2, 4.9, 15, 142, "Disponible hoy", "22331"),
        Medico(7, "Dr. Jorge Ramos", 2, 4.4, 9, 96, "Disponible mañana", "22332"),
        Medico(8, "Dra. Sofía Mendoza", 2, 4.6, 11, 118, "Disponible esta semana", "22333"),
        // Ginecología
        Medico(15, "Dra. Ana Torres", 3, 4.9, 12, 120, "Disponible hoy", "12345"),
        Medico(16, "Dra. María Gómez", 3, 4.8, 10, 105, "Disponible mañana", "12346"),
        Medico(17, "Dra. Patricia Ríos", 3, 4.7, 11, 99, "Disponible esta semana", "12347"),
        // Cardiología
        Medico(1, "Dra. Ana Ruiz", 4, 4.8, 12, 130, "Disponible hoy", "33441"),
        Medico(2, "Dr. Luis Soto", 4, 4.5, 8, 88, "Disponible mañana", "33442"),
        Medico(6, "Dr. Marco Quispe", 4, 4.7, 10, 101, "Disponible esta semana", "33443"),
        // Dermatología
        Medico(4, "Dr. Pedro Vega", 5, 4.2, 6, 74, "Disponible hoy", "44551"),
        Medico(9, "Dra. Lucía Torres", 5, 4.8, 14, 126, "Disponible mañana", "44552"),
        Medico(10, "Dr. Andrés Flores", 5, 4.3, 7, 81, "Disponible esta semana", "44553"),
        // Traumatología / Oftalmología
        Medico(5, "Dra. Elena Paz", 6, 4.6, 10, 93, "Disponible hoy", "55661"),
        Medico(11, "Dr. Renato Salas", 7, 4.5, 9, 90, "Disponible mañana", "66771")
    )
    private val horariosBase = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00",
        "10:30", "11:00", "11:30", "12:00"
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
    private var siguienteCitaId = 1

    var usuarioActual: Usuario? = null
        private set

    var sedeSeleccionada: String? = null

    fun registrarUsuario(nombre: String, correo: String, telefono: String, clave: String): Boolean {
        if (usuarios.any { it.correo.equals(correo, ignoreCase = true) }) return false
        val id = (usuarios.maxOfOrNull { it.id } ?: 0) + 1
        val nuevo = Usuario(id, nombre, correo, telefono, clave)
        usuarios.add(nuevo)
        // No inicia sesión automáticamente: el usuario debe loguearse después.
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
