package com.tecsup.huellitasvet.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.ui.components.UsuarioAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.dueno.FilaSwitch
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun PerfilAdminScreen(
    usuario: Usuario,
    sesion: SesionViewModel,
    onCerrar: () -> Unit
) {
    val oscuro by sesion.modoOscuro.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UsuarioAvatar(usuario.nombre, tamano = 88.dp)
        Spacer(Modifier.height(10.dp))
        Text(usuario.nombreCompleto(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Administrador", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DatoAdmin("Código", usuario.codigo)
                DatoAdmin("Nombre", usuario.nombre)
                DatoAdmin("Apellidos", usuario.apellidos)
                DatoAdmin("Correo electrónico", usuario.correo)
            }
        }
        Spacer(Modifier.height(20.dp))
        FilaSwitch("Modo oscuro", "Cambia el tema de la app", oscuro) { sesion.guardarModoOscuro(it) }
        Spacer(Modifier.height(28.dp))
        VetButton("Cerrar sesión", onClick = onCerrar, outlined = true, containerColor = Color(0xFFDC2626))
    }
}

@Composable
private fun DatoAdmin(etiqueta: String, valor: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(etiqueta, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor.ifBlank { "—" }, fontWeight = FontWeight.SemiBold)
    }
}
