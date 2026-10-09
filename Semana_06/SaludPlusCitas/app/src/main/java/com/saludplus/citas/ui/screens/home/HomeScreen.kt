package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.IconoCircularPastel
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulGris
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.AzulTexto
import com.saludplus.citas.ui.theme.GrisBorde
import com.saludplus.citas.ui.theme.MoradoSuave
import com.saludplus.citas.ui.theme.MoradoTexto
import com.saludplus.citas.ui.theme.NaranjaSuave
import com.saludplus.citas.ui.theme.NaranjaTexto
import com.saludplus.citas.ui.theme.VerdeSuave
import com.saludplus.citas.ui.theme.VerdeTexto
import kotlinx.coroutines.launch

private data class AccesoHome(
    val titulo: String,
    val icono: ImageVector,
    val fondo: Color,
    val tint: Color,
    val onClick: () -> Unit
)

@Composable
fun HomeScreen(
    onAgendarCita: () -> Unit,
    onMisDatos: () -> Unit,
    onResultados: () -> Unit,
    onSedes: () -> Unit,
    onMisMedicos: () -> Unit,
    onMisCitas: () -> Unit,
    onPerfil: () -> Unit,
    onNotificaciones: () -> Unit
) {
    var tab by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val nombre = Repositorio.usuarioActual?.nombre?.substringBefore(" ") ?: "Juan"
    val destacadas = Repositorio.especialidadesDestacadas()
    val accesos = listOf(
        AccesoHome("Agendar cita", Icons.Default.CalendarMonth, AzulClaro, AzulPrimario, onAgendarCita),
        AccesoHome("Mis citas", Icons.Default.EventAvailable, VerdeSuave, VerdeTexto, onMisCitas),
        AccesoHome("Mis datos", Icons.Default.Person, MoradoSuave, MoradoTexto, onMisDatos),
        AccesoHome("Resultados", Icons.Default.Description, NaranjaSuave, NaranjaTexto, onResultados)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "SaludPlus",
                    modifier = Modifier.padding(24.dp),
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto,
                    fontSize = 20.sp
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Sedes") },
                    selected = false,
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        onSedes()
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Mis Médicos") },
                    selected = false,
                    icon = { Icon(Icons.Default.MedicalServices, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        onMisMedicos()
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Agendar cita") },
                    selected = false,
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        onAgendarCita()
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = tab == 0,
                        onClick = { tab = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Inicio") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AzulPrimario,
                            selectedTextColor = AzulPrimario,
                            indicatorColor = AzulClaro
                        )
                    )
                    NavigationBarItem(
                        selected = tab == 1,
                        onClick = { tab = 1; onMisCitas() },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                        label = { Text("Citas") }
                    )
                    NavigationBarItem(
                        selected = tab == 2,
                        onClick = { tab = 2; onResultados() },
                        icon = { Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null) },
                        label = { Text("Resultados") }
                    )
                    NavigationBarItem(
                        selected = tab == 3,
                        onClick = { tab = 3; onPerfil() },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Perfil") }
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú", tint = AzulTexto)
                    }
                    IconButton(onClick = onNotificaciones) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = AzulTexto)
                    }
                }
                Text(
                    text = "¡Hola, $nombre!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulTexto
                )
                Text(
                    text = "¿Qué deseas hacer hoy?",
                    color = AzulGris,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AccesoCard(accesos[0], Modifier.weight(1f))
                    AccesoCard(accesos[1], Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AccesoCard(accesos[2], Modifier.weight(1f))
                    AccesoCard(accesos[3], Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Especialidades destacadas",
                        fontWeight = FontWeight.Bold,
                        color = AzulTexto,
                        fontSize = 18.sp
                    )
                    TextButton(onClick = onAgendarCita) {
                        Text("Ver todas", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
                    }
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp, top = 4.dp)
                ) {
                    items(destacadas, key = { it.id }) { esp ->
                        Card(
                            onClick = onAgendarCita,
                            modifier = Modifier.width(120.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, GrisBorde)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconoCircularPastel(
                                    icon = iconoEspecialidad(esp.nombre),
                                    tint = Color(esp.colorIcono),
                                    fondo = Color(esp.colorFondo)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = esp.nombre,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AzulTexto,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccesoCard(acceso: AccesoHome, modifier: Modifier = Modifier) {
    Card(
        onClick = acceso.onClick,
        modifier = modifier.aspectRatio(1.05f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = acceso.fondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = acceso.icono,
                contentDescription = acceso.titulo,
                tint = acceso.tint,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = acceso.titulo,
                color = acceso.tint,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun iconoEspecialidad(nombre: String): ImageVector = when {
    nombre.contains("Pedi", ignoreCase = true) -> Icons.Default.ChildCare
    nombre.contains("Gine", ignoreCase = true) -> Icons.Default.Favorite
    nombre.contains("Cardio", ignoreCase = true) -> Icons.Default.Favorite
    nombre.contains("Oftal", ignoreCase = true) -> Icons.Default.Visibility
    nombre.contains("Trauma", ignoreCase = true) -> Icons.Default.LocalHospital
    nombre.contains("Derma", ignoreCase = true) -> Icons.Default.Face
    else -> Icons.Default.Person
}
