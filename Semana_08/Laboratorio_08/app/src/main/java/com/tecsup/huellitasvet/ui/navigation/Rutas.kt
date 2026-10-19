package com.tecsup.huellitasvet.ui.navigation

object Rutas {
    const val Splash = "splash"
    const val Login = "login"
    const val Dueno = "dueno"
    const val Vet = "vet"

    const val Inicio = "inicio"
    const val Mascotas = "mascotas"
    const val Citas = "citas"
    const val Perfil = "perfil"
    const val FormMascota = "form_mascota?id={id}"
    const val Ficha = "ficha/{id}"
    const val PedirCita = "pedir_cita?mascotaId={mascotaId}"

    const val Panel = "panel"
    const val Agenda = "agenda"
    const val Pacientes = "pacientes"
    const val Servicios = "servicios"
    const val Ajustes = "ajustes"
    const val FormServicio = "form_servicio?id={id}"
    const val Atender = "atender/{id}"
    const val FichaPaciente = "ficha_paciente/{id}"

    fun formMascota(id: Int = 0) = "form_mascota?id=$id"
    fun ficha(id: Int) = "ficha/$id"
    fun pedirCita(mascotaId: Int = 0) = "pedir_cita?mascotaId=$mascotaId"
    fun formServicio(id: Int = 0) = "form_servicio?id=$id"
    fun atender(id: Int) = "atender/$id"
    fun fichaPaciente(id: Int) = "ficha_paciente/$id"
}
