package com.rojastuesta.tecsupstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rojastuesta.tecsupstore.ui.theme.Lavanda
import com.rojastuesta.tecsupstore.ui.theme.Morado

enum class DestinoTienda(val titulo: String) {
    Inicio("Inicio"),
    Pedidos("Mis pedidos"),
    Favoritos("Favoritos"),
    Perfil("Perfil"),
    CerrarSesion("Cerrar sesion")
}

@Composable
fun AppDrawer(
    destinoActual: DestinoTienda,
    onDestino: (DestinoTienda) -> Unit
) {
    ModalDrawerSheet {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Lavanda),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MR",
                    color = Morado,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Maria Rojas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "maria@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
        DestinoTienda.entries.forEach { destino ->
            val activo = destino == destinoActual
            NavigationDrawerItem(
                label = {
                    Text(
                        text = destino.titulo,
                        fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = activo,
                onClick = { onDestino(destino) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = null,
                        tint = if (activo) Morado else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Lavanda,
                    selectedIconColor = Morado,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContainerColor = Color.Transparent
                )
            )
        }
    }
}
