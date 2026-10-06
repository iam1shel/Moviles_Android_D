package com.saludplus.citas.navigation

object Rutas {
    const val Splash = "splash"
    const val Registro = "registro"
    const val Login = "login"
    const val Terminos = "terminos"
    const val Home = "home"
    const val Especialidades = "especialidades"
    const val Medicos = "medicos/{especialidadId}"
    const val FechaHora = "fecha_hora/{especialidadId}/{medicoId}"
    const val Confirmar = "confirmar/{especialidadId}/{medicoId}/{fecha}/{hora}"
    const val CitaExitosa = "cita_exitosa/{citaId}"
    const val MisCitas = "mis_citas"
    const val DetalleCita = "detalle_cita/{citaId}"
    const val Perfil = "perfil"
    const val Resultados = "resultados"
    const val Notificaciones = "notificaciones"

    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(especialidadId: Int, medicoId: Int) = "fecha_hora/$especialidadId/$medicoId"
    fun confirmar(especialidadId: Int, medicoId: Int, fecha: String, hora: String) =
        "confirmar/$especialidadId/$medicoId/$fecha/$hora"
    fun citaExitosa(citaId: Int) = "cita_exitosa/$citaId"
    fun detalleCita(citaId: Int) = "detalle_cita/$citaId"
}
