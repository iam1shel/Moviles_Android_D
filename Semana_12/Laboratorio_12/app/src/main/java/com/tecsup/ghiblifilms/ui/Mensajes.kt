package com.tecsup.ghiblifilms.ui

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

fun mensajeDeError(e: Throwable): String = when (e) {
    is IOException -> "No hay conexión a internet"
    is HttpException -> "El servidor respondió con error ${e.code()}"
    is SerializationException -> "La respuesta del servidor no tiene el formato esperado"
    else -> "Ocurrió un error inesperado"
}
