package com.tecsup.ghiblifilms.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.ghiblifilms.domain.model.Item
import com.tecsup.ghiblifilms.ui.UiState
import com.tecsup.ghiblifilms.ui.components.Cargando
import com.tecsup.ghiblifilms.ui.components.DoradoPuntaje
import com.tecsup.ghiblifilms.ui.components.ErrorView
import com.tecsup.ghiblifilms.ui.components.RosaFavorito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    vm: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val pelicula = (estado as? UiState.Exito)?.datos

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(pelicula?.titulo ?: "Detalle", maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (pelicula != null) {
                        IconButton(onClick = vm::alternarFavorito) {
                            Icon(
                                if (pelicula.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (pelicula.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                                tint = if (pelicula.esFavorito) RosaFavorito else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        val modifier = Modifier.padding(padding)
        when (val actual = estado) {
            UiState.Cargando -> Cargando(modifier, texto = "Cargando detalle…")
            is UiState.Error -> ErrorView(actual.mensaje, onReintentar = vm::cargar, modifier = modifier)
            is UiState.Exito -> DetalleContenido(actual.datos, modifier)
        }
    }
}

@Composable
private fun DetalleContenido(pelicula: Item, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = pelicula.bannerUrl ?: pelicula.imagenUrl,
            contentDescription = "Banner de ${pelicula.titulo}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = pelicula.imagenUrl,
                contentDescription = "Póster de ${pelicula.titulo}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 100.dp, height = 150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(pelicula.titulo, style = MaterialTheme.typography.headlineSmall)
                pelicula.tituloOriginal?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Dato(Icons.Filled.CalendarMonth, pelicula.anio)
                    pelicula.duracionMin?.let { Dato(Icons.Filled.Schedule, "$it min") }
                }
                Dato(Icons.Filled.Star, "${pelicula.puntaje}% Rotten Tomatoes", DoradoPuntaje)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Sinopsis", style = MaterialTheme.typography.titleMedium)
            Text(
                pelicula.descripcion ?: "Sin descripción disponible.",
                style = MaterialTheme.typography.bodyLarge
            )
            Text("Ficha técnica", style = MaterialTheme.typography.titleMedium)
            Fila("Director", pelicula.director)
            pelicula.productor?.let { Fila("Productor", it) }
            Fila("Estreno", pelicula.anio)
            pelicula.duracionMin?.let { Fila("Duración", "$it minutos") }
        }
    }
}

@Composable
private fun Dato(
    icono: ImageVector,
    texto: String,
    tinte: Color = MaterialTheme.colorScheme.primary
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = tinte, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(texto, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun Fila(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(96.dp)
        )
        Text(valor, style = MaterialTheme.typography.bodyMedium)
    }
}
