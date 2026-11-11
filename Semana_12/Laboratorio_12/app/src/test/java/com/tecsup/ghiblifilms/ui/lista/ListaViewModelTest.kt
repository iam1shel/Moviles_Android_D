package com.tecsup.ghiblifilms.ui.lista

import com.tecsup.ghiblifilms.MainDispatcherRule
import com.tecsup.ghiblifilms.fakes.FakeItemRepository
import com.tecsup.ghiblifilms.fakes.FakeNotificador
import com.tecsup.ghiblifilms.fakes.pelicula
import com.tecsup.ghiblifilms.ui.UiState
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ListaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val notificador = FakeNotificador()

    @Test
    fun `refresco exitoso muestra la lista y notifica cuantas se guardaron`() = runTest {
        val descargadas = listOf(pelicula("1"), pelicula("2"), pelicula("3"))
        val repo = FakeItemRepository(resultadoRefresco = Result.success(descargadas))

        val vm = ListaViewModel(repo, notificador)

        assertEquals(UiState.Exito(descargadas), vm.estado.value)
        assertEquals(listOf("Datos actualizados" to "Se guardaron 3 películas"), notificador.mostrados)
        assertNull(vm.aviso.value)
    }

    @Test
    fun `sin internet y sin datos guardados muestra error con mensaje claro`() = runTest {
        val repo = FakeItemRepository(resultadoRefresco = Result.failure(IOException()))

        val vm = ListaViewModel(repo, notificador)

        assertEquals(UiState.Error("No hay conexión a internet"), vm.estado.value)
        assertTrue(notificador.mostrados.isEmpty())
    }

    @Test
    fun `sin internet con datos guardados muestra lo guardado y el aviso`() = runTest {
        val guardadas = listOf(pelicula("1"), pelicula("2"))
        val repo = FakeItemRepository(guardadas, Result.failure(IOException()))

        val vm = ListaViewModel(repo, notificador)

        assertEquals(UiState.Exito(guardadas), vm.estado.value)
        assertEquals("Sin conexión: mostrando datos guardados", vm.aviso.value)
    }

    @Test
    fun `respuesta vacia termina en estado vacio y no se queda cargando`() = runTest {
        val repo = FakeItemRepository(resultadoRefresco = Result.success(emptyList()))

        val vm = ListaViewModel(repo, notificador)

        assertEquals(UiState.Exito(emptyList<Any>()), vm.estado.value)
    }

    @Test
    fun `error del servidor muestra el codigo http`() = runTest {
        val error = HttpException(Response.error<Any>(500, "".toResponseBody(null)))
        val repo = FakeItemRepository(resultadoRefresco = Result.failure(error))

        val vm = ListaViewModel(repo, notificador)

        assertEquals(UiState.Error("El servidor respondió con error 500"), vm.estado.value)
    }

    @Test
    fun `reintentar despues de un error carga la lista`() = runTest {
        val repo = FakeItemRepository(resultadoRefresco = Result.failure(IOException()))
        val vm = ListaViewModel(repo, notificador)
        assertTrue(vm.estado.value is UiState.Error)

        repo.resultadoRefresco = Result.success(listOf(pelicula("1")))
        vm.refrescar()

        assertEquals(UiState.Exito(listOf(pelicula("1"))), vm.estado.value)
        assertEquals(2, repo.llamadasRefrescar)
    }

    @Test
    fun `los favoritos se conservan al sincronizar otra vez`() = runTest {
        val descargadas = listOf(pelicula("1"), pelicula("2"))
        val repo = FakeItemRepository(resultadoRefresco = Result.success(descargadas))
        val vm = ListaViewModel(repo, notificador)

        vm.alternarFavorito("2")
        vm.refrescar()

        val lista = (vm.estado.value as UiState.Exito).datos
        assertEquals(listOf(false, true), lista.map { it.esFavorito })
    }
}
