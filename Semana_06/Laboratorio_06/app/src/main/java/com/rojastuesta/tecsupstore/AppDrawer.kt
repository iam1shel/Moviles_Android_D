package com.rojastuesta.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun AppDrawer(
    destinoActual: DestinoTienda,
    onDestino: (DestinoTienda) -> Unit
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MR",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Maria Rojas", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "maria@tecsup.edu.pe",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider()
        DestinoTienda.entries.forEach { destino ->
            val activo = destino == destinoActual
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                selected = activo,
                onClick = { onDestino(destino) },
                icon = {
                    Icon(
                        imageVector = destino.icono,
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedContainerColor = Color.Transparent
                )
            )
        }
    }
}
