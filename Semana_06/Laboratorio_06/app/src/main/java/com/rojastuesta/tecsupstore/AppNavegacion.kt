package com.rojastuesta.tecsupstore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private data class Pedido(val codigo: String, val detalle: String, val estado: String)

private val pedidos = listOf(
    Pedido("PED-1042", "Polo TECSUP · 1", "En camino"),
    Pedido("PED-1038", "Cuaderno de laboratorio · 2", "Entregado"),
    Pedido("PED-1031", "Taza del taller · 1", "Listo para recoger")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destino by remember { mutableStateOf(DestinoTienda.Inicio) }
    val titulo = if (destino == DestinoTienda.Inicio) "TECSUP Store" else destino.titulo

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onDestino = { elegido ->
                    destino = elegido
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(titulo) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            when (destino) {
                DestinoTienda.Inicio -> ListaProductos(padding)
                DestinoTienda.Pedidos -> PantallaPedidos(padding)
                DestinoTienda.Favoritos -> PantallaFavoritos(padding)
                DestinoTienda.Perfil -> PantallaPerfil(padding)
                DestinoTienda.CerrarSesion -> PantallaCerrarSesion(
                    padding = padding,
                    onIngresar = { destino = DestinoTienda.Inicio }
                )
            }
        }
    }
}

@Composable
private fun ListaProductos(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(catalogoTecsup, key = { it.id }) { producto ->
            TarjetaProducto(producto)
        }
    }
}

@Composable
private fun PantallaPedidos(padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pedidos, key = { it.codigo }) { pedido ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = pedido.codigo, style = MaterialTheme.typography.titleMedium)
                    Text(text = pedido.detalle, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = pedido.estado,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun PantallaFavoritos(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Favoritos", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "Marca Favoritos en el menú de una tarjeta. El cambio se queda en esa tarjeta y las demás no se alteran.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PantallaPerfil(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Maria Rojas", style = MaterialTheme.typography.headlineSmall)
        Text(text = "maria@tecsup.edu.pe", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "Alumna de TECSUP Store",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PantallaCerrarSesion(
    padding: PaddingValues,
    onIngresar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Sesión cerrada", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "Maria Rojas salió de TECSUP Store.",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onIngresar) {
            Text("Ingresar de nuevo")
        }
    }
}
