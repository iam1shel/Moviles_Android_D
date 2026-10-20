package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.UsuarioAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.dueno.FilaSwitch
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(
    usuario: Usuario,
    sesion: SesionViewModel,
    onCambiarPerfil: (Rol) -> Unit,
    onCerrar: () -> Unit
) {
    val oscuro by sesion.modoOscuro.collectAsState()
    val noti by sesion.notificaciones.collectAsState()
    val aceptar by sesion.aceptarCitas.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UsuarioAvatar(usuario.nombre, tamano = 88.dp)
        Spacer(Modifier.height(10.dp))
        Text(usuario.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(usuario.correo, color = Color(0xFF64748B), fontSize = 13.sp)
        Text("Perfil activo", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { sesion.cambiarPerfil(onCambiarPerfil) },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(18.dp)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.SwapHoriz, null, tint = Color.White)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Cambiar de perfil", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("Modo práctica: ir a vista Dueño", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                }
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, tint = Color.White)
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("Preferencias", fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Start))
        FilaSwitch("Modo oscuro", "Cambia el tema de la app", oscuro) { sesion.guardarModoOscuro(it) }
        FilaSwitch("Notificaciones", "Avisar citas nuevas", noti) { sesion.guardarNotificaciones(it) }
        FilaSwitch("Aceptar citas", "Abrir o cerrar la agenda", aceptar) { sesion.guardarAceptarCitas(it) }
        Text(
            "Se guardan en el dispositivo con DataStore",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF94A3B8),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(28.dp))
        VetButton("Cerrar sesión", onClick = onCerrar, outlined = true, containerColor = Color(0xFFDC2626))
    }
}
