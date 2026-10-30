package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.huellitasvet.VetApp
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.administrador.FormularioMascotaAdminScreen
import com.tecsup.huellitasvet.ui.administrador.MascotasAdminScreen
import com.tecsup.huellitasvet.ui.administrador.PerfilAdminScreen
import com.tecsup.huellitasvet.ui.dueno.FichaMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.MisCitasScreen
import com.tecsup.huellitasvet.ui.dueno.PedirCitaScreen
import com.tecsup.huellitasvet.ui.dueno.PerfilScreen
import com.tecsup.huellitasvet.ui.dueno.RegistroScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.AgendaScreen
import com.tecsup.huellitasvet.ui.veterinario.AjustesScreen
import com.tecsup.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.tecsup.huellitasvet.ui.veterinario.FormularioServicioScreen
import com.tecsup.huellitasvet.ui.veterinario.PacientesScreen
import com.tecsup.huellitasvet.ui.veterinario.PanelScreen
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.AdministracionViewModel
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

private fun irALogin(nav: NavHostController) {
    nav.navigate(Rutas.Login) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

private fun cerrarYVolverAlLogin(nav: NavHostController, sesion: SesionViewModel) {
    irALogin(nav)
    sesion.cerrarSesion()
}

@Composable
fun NavVet() {
    val app = LocalContext.current.applicationContext as VetApp
    val factory = remember { VetViewModelFactory(app.repo, app.prefs) }
    val sesion: SesionViewModel = viewModel(factory = factory)
    val oscuro by sesion.modoOscuro.collectAsState()
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rutas.Splash) {
        composable(Rutas.Splash) {
            VetTheme(darkTheme = oscuro) {
                SplashScreen(
                    vm = sesion,
                    onLogin = { nav.navigate(Rutas.Login) { popUpTo(Rutas.Splash) { inclusive = true } } },
                    onDueno = { nav.navigate(Rutas.Dueno) { popUpTo(Rutas.Splash) { inclusive = true } } },
                    onVet = { nav.navigate(Rutas.Vet) { popUpTo(Rutas.Splash) { inclusive = true } } },
                    onAdmin = { nav.navigate(Rutas.Admin) { popUpTo(Rutas.Splash) { inclusive = true } } }
                )
            }
        }
        composable(Rutas.Login) { entry ->
            val correoRegistrado = entry.savedStateHandle.get<String>("correoRegistrado")
            val claveRegistrada = entry.savedStateHandle.get<String>("claveRegistrada")
            LoginScreen(
                vm = sesion,
                modoOscuro = oscuro,
                correoInicial = correoRegistrado,
                claveInicial = claveRegistrada,
                onRegistrar = { nav.navigate(Rutas.Registro) },
                onIngreso = { rol ->
                    val dest = when (rol) {
                        Rol.ADMIN -> Rutas.Vet
                        Rol.ADMINISTRADOR -> Rutas.Admin
                        Rol.CLIENTE -> Rutas.Dueno
                    }
                    nav.navigate(dest) { popUpTo(Rutas.Login) { inclusive = true } }
                }
            )
        }
        composable(Rutas.Registro) {
            RegistroScreen(
                vm = sesion,
                modoOscuro = oscuro,
                onBack = { nav.popBackStack() },
                onCreada = { correo, clave ->
                    nav.previousBackStackEntry?.savedStateHandle?.set("correoRegistrado", correo)
                    nav.previousBackStackEntry?.savedStateHandle?.set("claveRegistrada", clave)
                    nav.popBackStack()
                }
            )
        }
        composable(Rutas.Dueno) {
            val usuario = sesion.usuarioActual.collectAsState().value
            if (usuario == null) {
                LaunchedEffect(Unit) { irALogin(nav) }
                return@composable
            }
            VetTheme(rol = Rol.CLIENTE, darkTheme = oscuro) {
                GrafoDueno(
                    usuario = usuario,
                    sesion = sesion,
                    onCambiarPerfil = { _ ->
                        nav.navigate(Rutas.Vet) { popUpTo(0) { inclusive = true } }
                    },
                    onCerrar = { cerrarYVolverAlLogin(nav, sesion) }
                )
            }
        }
        composable(Rutas.Vet) {
            val usuario = sesion.usuarioActual.collectAsState().value
            if (usuario == null) {
                LaunchedEffect(Unit) { irALogin(nav) }
                return@composable
            }
            VetTheme(rol = Rol.ADMIN, darkTheme = oscuro) {
                GrafoVeterinario(
                    usuario = usuario,
                    sesion = sesion,
                    onCambiarPerfil = { _ ->
                        nav.navigate(Rutas.Dueno) { popUpTo(0) { inclusive = true } }
                    },
                    onCerrar = { cerrarYVolverAlLogin(nav, sesion) }
                )
            }
        }
        composable(Rutas.Admin) {
            val usuario = sesion.usuarioActual.collectAsState().value
            if (usuario == null) {
                LaunchedEffect(Unit) { irALogin(nav) }
                return@composable
            }
            VetTheme(rol = Rol.ADMINISTRADOR, darkTheme = oscuro) {
                GrafoAdministrador(
                    usuario = usuario,
                    sesion = sesion,
                    onCerrar = { cerrarYVolverAlLogin(nav, sesion) }
                )
            }
        }
    }
}

private data class Destino(val ruta: String, val etiqueta: String, val icono: ImageVector)

@Composable
private fun GrafoDueno(
    usuario: Usuario,
    sesion: SesionViewModel,
    onCambiarPerfil: (Rol) -> Unit,
    onCerrar: () -> Unit
) {
    val app = LocalContext.current.applicationContext as VetApp
    val nav = rememberNavController()
    val tabs = listOf(
        Destino(Rutas.Inicio, "Inicio", Icons.Outlined.Home),
        Destino(Rutas.Mascotas, "Mascotas", Icons.Outlined.Pets),
        Destino(Rutas.Citas, "Citas", Icons.Outlined.CalendarMonth),
        Destino(Rutas.Perfil, "Perfil", Icons.Outlined.Person)
    )
    val actual = nav.currentBackStackEntryAsState().value?.destination?.route
    val mostrarBarra = tabs.any { actual == it.ruta }
    val factoryInicio = remember(usuario.id) { VetViewModelFactory(app.repo, app.prefs, duenoId = usuario.id) }
    val inicioVm: InicioViewModel = viewModel(key = "inicio-${usuario.id}", factory = factoryInicio)
    val citasVm: MisCitasViewModel = viewModel(key = "citas-${usuario.id}", factory = factoryInicio)

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEach { d ->
                        NavigationBarItem(
                            selected = actual == d.ruta,
                            onClick = {
                                nav.navigate(d.ruta) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(d.icono, contentDescription = d.etiqueta) },
                            label = { Text(d.etiqueta) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = Rutas.Inicio, modifier = Modifier.padding(padding)) {
            composable(Rutas.Inicio) {
                InicioScreen(
                    usuario = usuario,
                    vm = inicioVm,
                    onMascota = { nav.navigate(Rutas.ficha(it)) },
                    onAgregar = { nav.navigate(Rutas.formMascota()) },
                    onPedirCita = { nav.navigate(Rutas.pedirCita()) },
                    onVerTodas = {
                        nav.navigate(Rutas.Mascotas) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Rutas.Mascotas) {
                MascotasScreen(
                    vm = inicioVm,
                    onMascota = { nav.navigate(Rutas.ficha(it)) },
                    onAgregar = { nav.navigate(Rutas.formMascota()) }
                )
            }
            composable(Rutas.Citas) {
                MisCitasScreen(vm = citasVm, onPedirCita = { nav.navigate(Rutas.pedirCita()) })
            }
            composable(Rutas.Perfil) {
                PerfilScreen(usuario, sesion, inicioVm, citasVm, onCambiarPerfil, onCerrar)
            }
            composable(
                Rutas.FormMascota,
                arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val factory = VetViewModelFactory(app.repo, app.prefs, mascotaId = id)
                val vm: MascotaViewModel = viewModel(key = "form-m-$id", factory = factory)
                FormularioMascotaScreen(vm, usuario.id, id) { nav.popBackStack() }
            }
            composable(
                Rutas.Ficha,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val factory = VetViewModelFactory(app.repo, app.prefs, mascotaId = id)
                val vm: MascotaViewModel = viewModel(key = "ficha-$id", factory = factory)
                FichaMascotaScreen(
                    vm = vm,
                    onBack = { nav.popBackStack() },
                    onEditar = { nav.navigate(Rutas.formMascota(it)) },
                    onPedirCita = { nav.navigate(Rutas.pedirCita(it)) }
                )
            }
            composable(
                Rutas.PedirCita,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType; defaultValue = 0 })
            ) { entry ->
                val mid = entry.arguments?.getInt("mascotaId") ?: 0
                val factory = VetViewModelFactory(app.repo, app.prefs, duenoId = usuario.id, mascotaId = mid)
                val vm: PedirCitaViewModel = viewModel(key = "pedir-$mid", factory = factory)
                PedirCitaScreen(vm, onBack = { nav.popBackStack() }, onCreada = {
                    nav.navigate(Rutas.Citas) {
                        popUpTo(Rutas.Inicio)
                        launchSingleTop = true
                    }
                })
            }
        }
    }
}

@Composable
private fun GrafoVeterinario(
    usuario: Usuario,
    sesion: SesionViewModel,
    onCambiarPerfil: (Rol) -> Unit,
    onCerrar: () -> Unit
) {
    val app = LocalContext.current.applicationContext as VetApp
    val nav = rememberNavController()
    val tabs = listOf(
        Destino(Rutas.Panel, "Panel", Icons.Outlined.BarChart),
        Destino(Rutas.Agenda, "Agenda", Icons.Outlined.CalendarMonth),
        Destino(Rutas.Pacientes, "Pacientes", Icons.Outlined.Pets),
        Destino(Rutas.Servicios, "Servicios", Icons.Outlined.MedicalServices),
        Destino(Rutas.Ajustes, "Ajustes", Icons.Outlined.Settings)
    )
    val actual = nav.currentBackStackEntryAsState().value?.destination?.route
    val mostrarBarra = tabs.any { actual == it.ruta }
    val factory = remember { VetViewModelFactory(app.repo, app.prefs) }
    val serviciosVm: ServiciosViewModel = viewModel(key = "servicios", factory = factory)

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEach { d ->
                        NavigationBarItem(
                            selected = actual == d.ruta,
                            onClick = {
                                nav.navigate(d.ruta) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(d.icono, contentDescription = d.etiqueta) },
                            label = { Text(d.etiqueta) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = Rutas.Panel, modifier = Modifier.padding(padding)) {
            composable(Rutas.Panel) {
                val vm: PanelViewModel = viewModel(factory = factory)
                PanelScreen(vm) {
                    nav.navigate(Rutas.Agenda) {
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
            composable(Rutas.Agenda) {
                val vm: AgendaViewModel = viewModel(factory = factory)
                AgendaScreen(vm) { nav.navigate(Rutas.atender(it)) }
            }
            composable(Rutas.Pacientes) {
                val vm: PacientesViewModel = viewModel(factory = factory)
                PacientesScreen(vm) { nav.navigate(Rutas.fichaPaciente(it)) }
            }
            composable(Rutas.Servicios) {
                ServiciosScreen(serviciosVm) { nav.navigate(Rutas.formServicio(it)) }
            }
            composable(Rutas.Ajustes) {
                AjustesScreen(usuario, sesion, onCambiarPerfil, onCerrar)
            }
            composable(
                Rutas.FormServicio,
                arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                FormularioServicioScreen(serviciosVm, id) { nav.popBackStack() }
            }
            composable(
                Rutas.Atender,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val f = VetViewModelFactory(app.repo, app.prefs, citaId = id)
                val vm: AtenderCitaViewModel = viewModel(key = "atender-$id", factory = f)
                AtenderCitaScreen(vm) { nav.popBackStack() }
            }
            composable(
                Rutas.FichaPaciente,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val f = VetViewModelFactory(app.repo, app.prefs, mascotaId = id)
                val vm: MascotaViewModel = viewModel(key = "ficha-vet-$id", factory = f)
                FichaMascotaScreen(
                    vm = vm,
                    onBack = { nav.popBackStack() },
                    onEditar = {},
                    onPedirCita = {},
                    esVeterinario = true
                )
            }
        }
    }
}

@Composable
private fun GrafoAdministrador(
    usuario: Usuario,
    sesion: SesionViewModel,
    onCerrar: () -> Unit
) {
    val app = LocalContext.current.applicationContext as VetApp
    val nav = rememberNavController()
    val tabs = listOf(
        Destino(Rutas.AdminMascotas, "Mascotas", Icons.Outlined.Pets),
        Destino(Rutas.AdminPerfil, "Perfil", Icons.Outlined.Person)
    )
    val actual = nav.currentBackStackEntryAsState().value?.destination?.route
    val mostrarBarra = tabs.any { actual == it.ruta }
    val factory = remember { VetViewModelFactory(app.repo, app.prefs) }
    val adminVm: AdministracionViewModel = viewModel(key = "admin-mascotas", factory = factory)
    val duenos by adminVm.duenos.collectAsState()

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    tabs.forEach { d ->
                        NavigationBarItem(
                            selected = actual == d.ruta,
                            onClick = {
                                nav.navigate(d.ruta) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(d.icono, contentDescription = d.etiqueta) },
                            label = { Text(d.etiqueta) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = Rutas.AdminMascotas, modifier = Modifier.padding(padding)) {
            composable(Rutas.AdminMascotas) {
                MascotasAdminScreen(
                    vm = adminVm,
                    onMascota = { nav.navigate(Rutas.adminFormMascota(it)) },
                    onAgregar = { nav.navigate(Rutas.adminFormMascota()) }
                )
            }
            composable(Rutas.AdminPerfil) {
                PerfilAdminScreen(usuario, sesion, onCerrar)
            }
            composable(
                Rutas.AdminFormMascota,
                arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = 0 })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val f = VetViewModelFactory(app.repo, app.prefs, mascotaId = id)
                val vm: MascotaViewModel = viewModel(key = "admin-form-$id", factory = f)
                FormularioMascotaAdminScreen(vm, duenos, id) { nav.popBackStack() }
            }
        }
    }
}
