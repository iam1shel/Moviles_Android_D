package com.tecsup.ghiblifilms.data.auth

import com.tecsup.ghiblifilms.domain.model.Sesion

/** Se usa cuando falta app/google-services.json: la app abre y avisa en vez de cerrarse. */
object AuthSinConfigurar : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> =
        Result.failure(IllegalStateException("Firebase no está configurado: agrega app/google-services.json"))

    override suspend fun sesionActual(): Sesion? = null

    override fun cerrarSesion() = Unit
}
