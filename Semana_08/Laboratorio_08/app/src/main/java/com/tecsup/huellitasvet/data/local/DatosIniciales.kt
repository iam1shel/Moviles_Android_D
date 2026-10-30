package com.tecsup.huellitasvet.data.local

import com.tecsup.huellitasvet.data.local.entity.Administrador
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Sexo
import java.time.LocalDate

object DatosIniciales {
    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() != 0) return

        val servicios = listOf(
            Servicio(id = 1, nombre = "Consulta general", descripcion = "Revisión clínica completa", duracionMin = 30, precio = 60.0, activo = true, icono = "heart"),
            Servicio(id = 2, nombre = "Vacunación", descripcion = "Vacuna triple felina o séxtuple", duracionMin = 30, precio = 45.0, activo = true, icono = "syringe"),
            Servicio(id = 3, nombre = "Baño y corte", descripcion = "Higiene general", duracionMin = 60, precio = 50.0, activo = true, icono = "scissors"),
            Servicio(id = 4, nombre = "Desparasitación", descripcion = "Tratamiento antiparasitario interno y externo", duracionMin = 15, precio = 25.0, activo = false, icono = "heart")
        )
        db.servicioDao().insertarTodos(servicios)

        val usuarios = listOf(
            Usuario(id = 1, nombre = "Ana García", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
            Usuario(id = 2, nombre = "Luis Ramos", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
            Usuario(id = 3, nombre = "Dra. Rojas", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
        )
        db.usuarioDao().insertarTodos(usuarios)
        db.administradorDao().insertar(administrador())

        val mascotas = listOf(
            Mascota(id = 1, duenoId = 1, nombre = "Luna", especie = Especie.GATO.name, raza = "Siamesa", anioNacimiento = 2023, fechaNacimiento = "2023-05-14", peso = 4.3, sexo = Sexo.HEMBRA.name),
            Mascota(id = 2, duenoId = 1, nombre = "Max", especie = Especie.PERRO.name, raza = "Labrador", anioNacimiento = 2022, fechaNacimiento = "2022-11-03", peso = 24.5, sexo = Sexo.MACHO.name),
            Mascota(id = 3, duenoId = 2, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Bulldog", anioNacimiento = 2018, fechaNacimiento = "2018-02-20", peso = 18.0, sexo = Sexo.MACHO.name),
            Mascota(id = 4, duenoId = 2, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo", anioNacimiento = 2024, fechaNacimiento = "2024-08-09", peso = 3.8, sexo = Sexo.HEMBRA.name)
        )
        db.mascotaDao().insertarTodos(mascotas)

        val hoy = LocalDate.now()
        fun fecha(dias: Long) = hoy.plusDays(dias).toString()

        val citas = listOf(
            Cita(mascotaId = 1, servicioId = 2, fecha = fecha(-20), hora = "10:00", estado = EstadoCita.ATENDIDA.name, precio = 45.0, diagnostico = "Vacuna triple felina. Próximo refuerzo en 12 meses.", tratamiento = "Vacuna triple felina", pesoRegistrado = 4.1),
            Cita(mascotaId = 1, servicioId = 1, fecha = fecha(-12), hora = "11:00", estado = EstadoCita.ATENDIDA.name, precio = 60.0, diagnostico = "Control de rutina, sin hallazgos. Buen estado general.", tratamiento = "Ninguno", pesoRegistrado = 4.2),
            Cita(mascotaId = 1, servicioId = 3, fecha = fecha(-5), hora = "15:00", estado = EstadoCita.ATENDIDA.name, precio = 50.0, diagnostico = "Higiene general", tratamiento = "Baño medicado", pesoRegistrado = 4.3),
            Cita(mascotaId = 2, servicioId = 1, fecha = fecha(-8), hora = "09:00", estado = EstadoCita.ATENDIDA.name, precio = 60.0, diagnostico = "Revisión anual. Condición corporal adecuada.", tratamiento = "Antiparasitario", pesoRegistrado = 24.5),
            Cita(mascotaId = 3, servicioId = 3, fecha = fecha(0), hora = "09:00", estado = EstadoCita.ATENDIDA.name, precio = 50.0, diagnostico = "Piel sana, sin nudos.", tratamiento = "Baño y corte", pesoRegistrado = 18.0),
            Cita(mascotaId = 1, servicioId = 2, fecha = fecha(0), hora = "10:30", estado = EstadoCita.CONFIRMADA.name, precio = 45.0),
            Cita(mascotaId = 3, servicioId = 1, fecha = fecha(0), hora = "11:00", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
            Cita(mascotaId = 4, servicioId = 1, fecha = fecha(0), hora = "12:30", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
            Cita(mascotaId = 2, servicioId = 1, fecha = fecha(2), hora = "11:00", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
            Cita(mascotaId = 2, servicioId = 3, fecha = fecha(5), hora = "09:30", estado = EstadoCita.PENDIENTE.name, precio = 50.0)
        )
        db.citaDao().insertarTodos(citas)
    }

    suspend fun asegurarAdministrador(db: VetDatabase) {
        if (db.administradorDao().contar() != 0) return
        db.administradorDao().insertar(administrador(id = 0))
    }

    private fun administrador(id: Int = 1) = Administrador(
        id = id,
        codigo = "ADM-001",
        nombre = "Camila",
        apellidos = "Torres",
        correo = "admin@huellitas.test",
        clave = "admin123"
    )
}
