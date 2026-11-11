package com.tecsup.ghiblifilms.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ghiblifilms.domain.model.Rol
import com.tecsup.ghiblifilms.domain.model.Sesion
import com.tecsup.ghiblifilms.ui.UiState
import com.tecsup.ghiblifilms.ui.components.AvisoBanner
import com.tecsup.ghiblifilms.ui.components.Cargando
import com.tecsup.ghiblifilms.ui.components.ErrorView
import com.tecsup.ghiblifilms.ui.components.PeliculaCard
import com.tecsup.ghiblifilms.ui.components.VacioView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    vm: ListaViewModel,
    sesion: Sesion,
    onAbrirDetalle: (String) -> Unit,
    onFavoritos: () -> Unit,
    onAdmin: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val aviso by vm.aviso.collectAsStateWithLifecycle()
    val refrescando by vm.refrescando.collectAsStateWithLifecycle()
    val esAdmin = sesion.rol == Rol.ADMIN

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Ghibli Films")
                        Text(
                            "${sesion.correo} · ${if (esAdmin) "Admin" else "Usuario"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    IconButton(onClick = vm::refrescar, enabled = !refrescando) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Sincronizar")
                    }
                    IconButton(onClick = onFavoritos) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                    }
                    if (esAdmin) {
                        IconButton(onClick = onAdmin) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = "Panel Admin")
                        }
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (refrescando && estado is UiState.Exito) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            aviso?.let { AvisoBanner(it) }

            when (val actual = estado) {
                UiState.Cargando -> Cargando(texto = "Cargando películas…")
                is UiState.Error -> ErrorView(actual.mensaje, onReintentar = vm::refrescar)
                is UiState.Exito -> if (actual.datos.isEmpty()) {
                    VacioView(
                        icono = Icons.Filled.LocalMovies,
                        titulo = "No hay películas guardadas",
                        texto = "Sincroniza para descargarlas desde internet.",
                        accion = "Sincronizar",
                        onAccion = vm::refrescar
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(actual.datos, key = { it.id }) { item ->
                            PeliculaCard(
                                item = item,
                                onClick = { onAbrirDetalle(item.id) },
                                onFavorito = { vm.alternarFavorito(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
