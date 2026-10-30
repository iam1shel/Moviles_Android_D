package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.UsuarioAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun InicioScreen(
    usuario: Usuario,
    vm: InicioViewModel,
    onMascota: (Int) -> Unit,
    onAgregar: () -> Unit,
    onPedirCita: () -> Unit,
    onVerTodas: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val proxima by vm.proximaCita.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val nombre = usuario.nombre.substringBefore(" ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Hola, $nombre", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("¿Cómo están tus mascotas hoy?", color = Color(0xFF94A3B8), fontSize = 14.sp)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Outlined.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            UsuarioAvatar(usuario.nombre, tamano = 48.dp)
        }
        Spacer(Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val sobrePrimario = MaterialTheme.colorScheme.onPrimary
            if (proxima == null) {
                Text("Aún no tienes citas", color = sobrePrimario, modifier = Modifier.padding(20.dp))
            } else {
                val c = proxima!!
                val fechaTxt = runCatching {
                    LocalDate.parse(c.cita.fecha).format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es")))
                }.getOrDefault(c.cita.fecha)
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Próxima cita", color = sobrePrimario.copy(alpha = 0.85f), fontSize = 12.sp)
                        Text(
                            "${c.mascota.mascota.nombre} · ${c.servicio.nombre}",
                            color = sobrePrimario,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Schedule, null, tint = sobrePrimario, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("$fechaTxt · ${c.cita.hora}", color = sobrePrimario, fontSize = 13.sp)
                        }
                        Spacer(Modifier.height(8.dp))
                        EstadoCitaChip(c.cita.estado, sobreOscuro = true)
                    }
                    MascotaAvatar(
                        especie = c.mascota.mascota.especie,
                        nombre = c.mascota.mascota.nombre,
                        tamano = 64.dp
                    )
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mis mascotas", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.weight(1f))
            TextButton(onClick = onVerTodas) { Text("Ver todas", color = MaterialTheme.colorScheme.primary) }
        }
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            mascotas.forEach { m -> TarjetaMascotaMini(m, onClick = { onMascota(m.id) }) }
            Card(
                onClick = onAgregar,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    Modifier.size(width = 100.dp, height = 128.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "Agregar", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Agregar", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Text("Servicios", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            servicios.take(3).forEach { s ->
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        IconoServicio(s.icono, 28.dp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(s.nombre.substringBefore(" "), fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        val aviso = mascotas.find { it.nombre.equals("Max", true) } ?: mascotas.firstOrNull()
        if (aviso != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E8)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFFFEDD5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.WarningAmber, null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Recordatorio", fontWeight = FontWeight.SemiBold, color = Color(0xFFD97706), fontSize = 13.sp)
                        Text("${aviso.nombre}: refuerzo de vacuna en 5 días", color = Color(0xFF92400E), fontSize = 13.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        VetButton("Pedir una cita", onClick = onPedirCita)
    }
}

@Composable
private fun TarjetaMascotaMini(m: Mascota, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 14.dp).width(88.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MascotaAvatar(m.especie, nombre = m.nombre, tamano = 56.dp)
            Spacer(Modifier.height(8.dp))
            Text(m.nombre, fontWeight = FontWeight.SemiBold)
            Text("Ver ficha", fontSize = 11.sp, color = Color(0xFF94A3B8))
        }
    }
}
