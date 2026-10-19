package com.tecsup.huellitasvet.ui.dueno

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
import androidx.compose.material3.Switch
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
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.UsuarioAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    usuario: Usuario,
    sesion: SesionViewModel,
    inicio: InicioViewModel,
    citas: MisCitasViewModel,
    onCambiarPerfil: (Rol) -> Unit,
    onCerrar: () -> Unit
) {
    val mascotas by inicio.mascotas.collectAsState()
    val proximas by citas.proximas.collectAsState()
    val historial by citas.historial.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val noti by sesion.notificaciones.collectAsState()
    val atenciones = historial.count { it.cita.estado == "ATENDIDA" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UsuarioAvatar(usuario.nombre, tamano = 88.dp)
        Spacer(Modifier.height(12.dp))
        Text(usuario.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(usuario.correo, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Contador("Mascotas", mascotas.size.toString(), Modifier.weight(1f))
            Contador("Próximas", proximas.size.toString(), Modifier.weight(1f))
            Contador("Atenciones", atenciones.toString(), Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))
        VetButton("Cambiar de perfil", onClick = { sesion.cambiarPerfil(onCambiarPerfil) })
        Spacer(Modifier.height(16.dp))
        FilaSwitch("Modo oscuro", "Cambia el tema de la app", oscuro) { sesion.guardarModoOscuro(it) }
        FilaSwitch("Recordatorios", "Avisar citas nuevas", noti) { sesion.guardarNotificaciones(it) }
        Spacer(Modifier.height(24.dp))
        VetButton("Cerrar sesión", onClick = onCerrar, outlined = true, containerColor = Color(0xFFDC2626))
    }
}

@Composable
fun Contador(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(titulo, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun FilaSwitch(
    titulo: String,
    subtitulo: String = "",
    valor: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.Medium)
            if (subtitulo.isNotEmpty()) {
                Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
            }
        }
        Switch(checked = valor, onCheckedChange = onChange)
    }
}
