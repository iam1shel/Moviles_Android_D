package com.tecsup.centrotareas.model

// Estados para el cálculo de números primos
sealed interface PrimosUi {

    data object Inicial : PrimosUi

    data object Calculando : PrimosUi

    data class Listo(
        val cantidad: Int,
        val milisegundos: Long
    ) : PrimosUi

    data object Cancelado : PrimosUi
}

// Estados para las cargas de datos
sealed interface CargaUi {

    data object Inicial : CargaUi

    data object Cargando : CargaUi

    data class Exito(
        val modo: String,
        val perfil: String,
        val pedidos: Int,
        val milisegundos: Long
    ) : CargaUi

    data class Error(
        val mensaje: String
    ) : CargaUi
}
