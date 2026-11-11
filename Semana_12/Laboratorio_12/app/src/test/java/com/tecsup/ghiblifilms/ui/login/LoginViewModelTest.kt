package com.tecsup.ghiblifilms.ui.login

import com.tecsup.ghiblifilms.MainDispatcherRule
import com.tecsup.ghiblifilms.domain.model.Rol
import com.tecsup.ghiblifilms.domain.model.Sesion
import com.tecsup.ghiblifilms.fakes.FakeAuthRepository
import com.tecsup.ghiblifilms.ui.UiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val admin = Sesion("uid-admin", "admin@ghibli.test", Rol.ADMIN)

    @Test
    fun `campos vacios muestran validacion y no llaman a Firebase`() = runTest {
        val auth = FakeAuthRepository()
        val vm = LoginViewModel(auth)

        vm.iniciarSesion("", "  ")

        assertEquals(UiState.Error("Ingresa tu correo y tu contraseña"), vm.estado.value)
        assertEquals(0, auth.llamadasLogin)
    }

    @Test
    fun `credenciales incorrectas muestran el error`() = runTest {
        val vm = LoginViewModel(FakeAuthRepository())

        vm.iniciarSesion("usuario@ghibli.test", "mala")

        assertEquals(UiState.Error("Correo o contraseña incorrectos"), vm.estado.value)
    }

    @Test
    fun `login correcto entrega la sesion con su rol`() = runTest {
        val vm = LoginViewModel(FakeAuthRepository(resultadoLogin = Result.success(admin)))

        vm.iniciarSesion("admin@ghibli.test", "123456")

        assertEquals(UiState.Exito(admin), vm.estado.value)
    }

    @Test
    fun `la sesion guardada se recupera al abrir la app`() = runTest {
        val vm = LoginViewModel(FakeAuthRepository(sesionGuardada = admin))

        assertTrue(vm.sesionVerificada.value)
        assertEquals(UiState.Exito(admin), vm.estado.value)
    }

    @Test
    fun `cerrar sesion vuelve al estado sin sesion`() = runTest {
        val auth = FakeAuthRepository(sesionGuardada = admin)
        val vm = LoginViewModel(auth)

        vm.cerrarSesion()

        assertTrue(auth.sesionCerrada)
        assertEquals(UiState.Exito(null), vm.estado.value)
    }
}
