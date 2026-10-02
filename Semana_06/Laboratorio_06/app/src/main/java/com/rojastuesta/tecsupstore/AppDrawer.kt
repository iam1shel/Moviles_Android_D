package com.rojastuesta.tecsupstore

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class DestinoTienda(
    val titulo: String,
    val icono: ImageVector
) {
    Inicio("Inicio", Icons.Default.Home),
    Pedidos("Mis pedidos", Icons.Default.ShoppingCart),
    Favoritos("Favoritos", Icons.Default.Favorite),
    Perfil("Perfil", Icons.Default.Person),
    CerrarSesion("Cerrar sesión", Icons.AutoMirrored.Filled.Logout)
}

@Composable
fun AppDrawer(onDestino: (DestinoTienda) -> Unit) {
    ModalDrawerSheet {
        Text(
            text = "Menú",
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        HorizontalDivider()
        DestinoTienda.entries.forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                selected = false,
                onClick = { onDestino(destino) },
                icon = {
                    Icon(
                        imageVector = destino.icono,
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
