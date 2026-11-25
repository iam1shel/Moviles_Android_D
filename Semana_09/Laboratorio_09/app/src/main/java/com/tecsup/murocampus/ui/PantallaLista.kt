package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    viewModel: PostsViewModel,
    alSeleccionarPost: (Int) -> Unit,
    alIrACrear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listaState by viewModel.lista.collectAsStateWithLifecycle()
    val filtro by viewModel.usuarioFiltro.collectAsStateWithLifecycle()
    val query by viewModel.queryBusqueda.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Campo de búsqueda debajo de la barra superior y encima de los filtros
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.actualizarBusqueda(it) },
                placeholder = { Text("Buscar publicación...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.actualizarBusqueda("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            // Fila horizontal de filtros con LazyRow
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = filtro == null,
                        onClick = { viewModel.filtrar(null) },
                        label = { Text("Todos") }
                    )
                }
                items(10) { index ->
                    val userId = index + 1
                    FilterChip(
                        selected = filtro == userId,
                        onClick = { viewModel.filtrar(userId) },
                        label = { Text("Usuario $userId") }
                    )
                }
            }

            // Contenido según UiState
            when (val state = listaState) {
                is UiState.Cargando -> {
                    Cargando(modifier = Modifier.weight(1f))
                }
                is UiState.Error -> {
                    ErrorConReintento(
                        mensaje = state.mensaje,
                        alReintentar = { viewModel.cargar() },
                        modifier = Modifier.weight(1f)
                    )
                }
                is UiState.Exito -> {
                    val posts = state.datos
                    if (posts.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se encontraron publicaciones", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(posts, key = { it.id }) { post ->
                                TarjetaPost(
                                    userId = post.userId,
                                    titulo = post.title,
                                    texto = post.body,
                                    alPulsar = { alSeleccionarPost(post.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = alIrACrear,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(Icons.Default.Add, contentDescription = "Crear publicación")
        }
    }
}
