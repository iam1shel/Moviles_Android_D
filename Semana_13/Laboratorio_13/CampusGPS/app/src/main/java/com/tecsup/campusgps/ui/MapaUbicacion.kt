package com.tecsup.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionadoId: Int?,
    masCercano: Lugar?,
    radioSeleccionado: Float,
    onLugarClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lima = LatLng(-12.0464, -77.0428)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lima, 12f)
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val userLatLng = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(userLatLng, 16f),
                durationMs = 1000
            )
        }
    }

    LaunchedEffect(seleccionadoId) {
        if (seleccionadoId != null) {
            val lugarEncontrado = lugares.find { it.lugar.id == seleccionadoId }?.lugar
            if (lugarEncontrado != null) {
                val lugarLatLng = LatLng(lugarEncontrado.latitud, lugarEncontrado.longitud)
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(lugarLatLng, 17f),
                    durationMs = 1000
                )
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        if (estado is UbicacionUi.Lista) {
            val userLatLng = LatLng(estado.posicion.latitud, estado.posicion.longitud)
            Marker(
                state = MarkerState(position = userLatLng),
                title = "Estás aquí",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )

            if (radioSeleccionado > 0f) {
                Circle(
                    center = userLatLng,
                    radius = radioSeleccionado.toDouble(),
                    fillColor = Color(0x221565A8),
                    strokeColor = Color(0xFF1565A8),
                    strokeWidth = 3f
                )
            }
        }

        for (item in lugares) {
            val lugar = item.lugar
            val latLng = LatLng(lugar.latitud, lugar.longitud)
            val isMasCercano = masCercano?.id == lugar.id
            val markerColor = if (isMasCercano) {
                BitmapDescriptorFactory.HUE_GREEN
            } else {
                BitmapDescriptorFactory.HUE_AZURE
            }

            androidx.compose.runtime.key(lugar.id) {
                Marker(
                    state = MarkerState(position = latLng),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    icon = BitmapDescriptorFactory.defaultMarker(markerColor),
                    onClick = {
                        onLugarClick(lugar.id)
                        false
                    }
                )
            }
        }
    }
}
