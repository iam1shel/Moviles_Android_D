package com.tecsup.campusgps.util

import android.location.Location
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.Posicion

fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultados = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultados
    )
    return resultados[0]
}
