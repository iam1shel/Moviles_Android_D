package com.tecsup.ghiblifilms.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.ghiblifilms.AppContainer
import com.tecsup.ghiblifilms.domain.model.Rol
import com.tecsup.ghiblifilms.domain.model.Sesion
import com.tecsup.ghiblifilms.ui.UiState
import com.tecsup.ghiblifilms.ui.admin.AdminScreen
import com.tecsup.ghiblifilms.ui.admin.AdminViewModel
import com.tecsup.ghiblifilms.ui.components.Cargando
import com.tecsup.ghiblifilms.ui.detalle.DetalleScreen
import com.tecsup.ghiblifilms.ui.detalle.DetalleViewModel
import com.tecsup.ghiblifilms.ui.favoritos.FavoritosScreen
import com.tecsup.ghiblifilms.ui.favoritos.FavoritosViewModel
import com.tecsup.ghiblifilms.ui.lista.ListaScreen
import com.tecsup.ghiblifilms.ui.lista.ListaViewModel
import com.tecsup.ghiblifilms.ui.login.LoginScreen
import com.tecsup.ghiblifilms.ui.login.LoginViewModel

object Rutas {
    const val LISTA = "lista"
    const val DETALLE = "detalle/{${DetalleViewModel.ARG_ID}}"
    const val FAVORITOS = "favoritos"
    const val ADMIN = "admin"

    fun detalle(id: String) = "detalle/$id"
}

@Composable
fun AppNav(container: AppContainer) {
    val loginVm: LoginViewModel = viewModel(factory = LoginViewModel.factory(container))
    val estado by loginVm.estado.collectAsStateWithLifecycle()
    val verificada by loginVm.sesionVerificada.collectAsStateWithLifecycle()
    val sesion = (estado as? UiState.Exito)?.datos

    when {
        !verificada -> Cargando(texto = "Revisando tu sesión…")
        sesion == null -> LoginScreen(estado = estado, onIngresar = loginVm::iniciarSesion)
        else -> CatalogoNav(container, sesion, onCerrarSesion = loginVm::cerrarSesion)
    }
}

@Composable
private fun CatalogoNav(
    container: AppContainer,
    sesion: Sesion,
    onCerrarSesion: () -> Unit
) {
    val nav = rememberNavController()
    val volver: () -> Unit = { nav.popBackStack() }

    NavHost(navController = nav, startDestination = Rutas.LISTA) {
        composable(Rutas.LISTA) {
            val vm: ListaViewModel = viewModel(factory = ListaViewModel.factory(container))
            ListaScreen(
                vm = vm,
                sesion = sesion,
                onAbrirDetalle = { nav.navigate(Rutas.detalle(it)) },
                onFavoritos = { nav.navigate(Rutas.FAVORITOS) { launchSingleTop = true } },
                onAdmin = { nav.navigate(Rutas.ADMIN) { launchSingleTop = true } },
                onCerrarSesion = onCerrarSesion
            )
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument(DetalleViewModel.ARG_ID) { type = NavType.StringType })
        ) {
            val vm: DetalleViewModel = viewModel(factory = DetalleViewModel.factory(container))
            DetalleScreen(vm = vm, onVolver = volver)
        }
        composable(Rutas.FAVORITOS) {
            val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModel.factory(container))
            FavoritosScreen(
                vm = vm,
                onAbrirDetalle = { nav.navigate(Rutas.detalle(it)) },
                onVolver = volver
            )
        }
        composable(Rutas.ADMIN) {
            if (sesion.rol == Rol.ADMIN) {
                val vm: AdminViewModel = viewModel(factory = AdminViewModel.factory(container))
                AdminScreen(vm = vm, onVolver = volver)
            }
        }
    }
}
