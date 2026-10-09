package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.citas.DetalleCitaScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.medicos.MisMedicosScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.Splash) {
        composable(Rutas.Splash) {
            SplashScreen(
                onRegistrarse = { navController.navigate(Rutas.Registro) },
                onIniciarSesion = { navController.navigate(Rutas.Login) }
            )
        }
        composable(Rutas.Registro) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onRegistrado = {
                    navController.navigate(Rutas.Home) {
                        popUpTo(Rutas.Splash) { inclusive = true }
                    }
                },
                onTerminos = { navController.navigate(Rutas.Terminos) }
            )
        }
        composable(Rutas.Login) {
            LoginScreen(
                onVolver = { navController.popBackStack() },
                onLoginOk = {
                    navController.navigate(Rutas.Home) {
                        popUpTo(Rutas.Splash) { inclusive = true }
                    }
                }
            )
        }
        composable(Rutas.Terminos) {
            TerminosScreen(onVolver = { navController.popBackStack() })
        }
        composable(Rutas.Home) {
            HomeScreen(
                onEspecialidades = { navController.navigate(Rutas.Especialidades) },
                onMisCitas = { navController.navigate(Rutas.MisCitas) },
                onResultados = { navController.navigate(Rutas.Resultados) },
                onPerfil = { navController.navigate(Rutas.Perfil) },
                onNotificaciones = { navController.navigate(Rutas.Notificaciones) },
                onMisMedicos = { navController.navigate(Rutas.MisMedicos) }
            )
        }
        composable(Rutas.MisMedicos) {
            MisMedicosScreen(onVolver = { navController.popBackStack() })
        }
        composable(Rutas.Especialidades) {
            EspecialidadesScreen(
                onVolver = { navController.popBackStack() },
                onSeleccionar = { id -> navController.navigate(Rutas.medicos(id)) }
            )
        }
        composable(
            route = Rutas.Medicos,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { entry ->
            val especialidadId = entry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(
                especialidadId = especialidadId,
                onVolver = { navController.popBackStack() },
                onSeleccionar = { medicoId ->
                    navController.navigate(Rutas.fechaHora(especialidadId, medicoId))
                }
            )
        }
        composable(
            route = Rutas.FechaHora,
            arguments = listOf(
                navArgument("especialidadId") { type = NavType.IntType },
                navArgument("medicoId") { type = NavType.IntType }
            )
        ) { entry ->
            val especialidadId = entry.arguments?.getInt("especialidadId") ?: 0
            val medicoId = entry.arguments?.getInt("medicoId") ?: 0
            FechaHoraScreen(
                especialidadId = especialidadId,
                medicoId = medicoId,
                onVolver = { navController.popBackStack() },
                onContinuar = { fecha, hora ->
                    navController.navigate(Rutas.confirmar(especialidadId, medicoId, fecha, hora))
                }
            )
        }
        composable(
            route = Rutas.Confirmar,
            arguments = listOf(
                navArgument("especialidadId") { type = NavType.IntType },
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { entry ->
            val especialidadId = entry.arguments?.getInt("especialidadId") ?: 0
            val medicoId = entry.arguments?.getInt("medicoId") ?: 0
            val fecha = entry.arguments?.getString("fecha").orEmpty()
            val hora = entry.arguments?.getString("hora").orEmpty()
            ConfirmarCitaScreen(
                especialidadId = especialidadId,
                medicoId = medicoId,
                fecha = fecha,
                hora = hora,
                onVolver = { navController.popBackStack() },
                onConfirmado = { citaId ->
                    navController.navigate(Rutas.citaExitosa(citaId)) {
                        popUpTo(Rutas.Home) { inclusive = false }
                    }
                }
            )
        }
        composable(
            route = Rutas.CitaExitosa,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { entry ->
            val citaId = entry.arguments?.getInt("citaId") ?: 0
            CitaExitosaScreen(
                citaId = citaId,
                onMisCitas = {
                    navController.navigate(Rutas.MisCitas) {
                        popUpTo(Rutas.Home) { inclusive = false }
                    }
                },
                onInicio = {
                    navController.navigate(Rutas.Home) {
                        popUpTo(Rutas.Home) { inclusive = true }
                    }
                }
            )
        }
        composable(Rutas.MisCitas) {
            MisCitasScreen(
                onVolver = { navController.popBackStack() },
                onDetalle = { id -> navController.navigate(Rutas.detalleCita(id)) }
            )
        }
        composable(
            route = Rutas.DetalleCita,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { entry ->
            DetalleCitaScreen(
                citaId = entry.arguments?.getInt("citaId") ?: 0,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.Perfil) {
            PerfilScreen(
                onCerrarSesion = {
                    navController.navigate(Rutas.Splash) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(Rutas.Resultados) {
            ResultadosScreen(onVolver = { navController.popBackStack() })
        }
        composable(Rutas.Notificaciones) {
            NotificacionesScreen(onVolver = { navController.popBackStack() })
        }
    }
}
