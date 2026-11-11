package com.tecsup.ghiblifilms.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.ghiblifilms.ui.UiState
import com.tecsup.ghiblifilms.ui.components.Cargando
import com.tecsup.ghiblifilms.ui.components.ErrorView
import com.tecsup.ghiblifilms.ui.components.PeliculaCard
import com.tecsup.ghiblifilms.ui.components.VacioView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    vm: FavoritosViewModel,
    onAbrirDetalle: (String) -> Unit,
    onVolver: () -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis favoritos") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        when (val actual = estado) {
            UiState.Cargando -> Cargando(modifier)
            is UiState.Error -> ErrorView(actual.mensaje, onReintentar = {}, modifier = modifier)
            is UiState.Exito -> if (actual.datos.isEmpty()) {
                VacioView(
                    icono = Icons.Filled.FavoriteBorder,
                    titulo = "Aún no tienes favoritos",
                    texto = "Toca el corazón de una película para guardarla aquí.",
                    modifier = modifier
                )
            } else {
                LazyColumn(
                    modifier = modifier,
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
