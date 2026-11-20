package com.tecsup.centrotareas

import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TareasViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeRepository : TareasRepository {
        var contarPrimosDelayMs: Long = 0

        override suspend fun contarPrimos(hasta: Int): Int {
            if (contarPrimosDelayMs > 0) {
                delay(contarPrimosDelayMs)
            }
            return 168
        }

        override suspend fun cargarPerfil(): String {
            return "Ana Torres (ficticia)"
        }

        override suspend fun cargarPedidos(simularError: Boolean): List<String> {
            if (simularError) throw IOException("Sin conexión")
            return listOf("Pedido 101", "Pedido 102", "Pedido 103")
        }

        override fun observarProgreso(): Flow<Float> = flow {
            emit(0.5f)
            emit(1.0f)
        }
    }

    private lateinit var fakeRepository: FakeRepository
    private lateinit var viewModel: TareasViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeRepository()
        viewModel = TareasViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun estadoInicial_esCorrecto() {
        assertEquals("Listo", viewModel.mensaje.value)
        assertEquals(0f, viewModel.progreso.value, 0.001f)
        assertEquals(false, viewModel.corriendo.value)
        assertEquals(PrimosUi.Inicial, viewModel.primos.value)
        assertEquals(CargaUi.Inicial, viewModel.carga.value)
        assertEquals(false, viewModel.simularError.value)
    }

    @Test
    fun tareaLarga_colectaProgresoRepository() = runTest {
        viewModel.tareaLarga()
        advanceUntilIdle()

        assertEquals(1.0f, viewModel.progreso.value, 0.001f)
        assertEquals("Terminó", viewModel.mensaje.value)
        assertEquals(false, viewModel.corriendo.value)
    }

    @Test
    fun contarPrimos_exito_actualizaEstadoListo() = runTest {
        viewModel.contar(1000)
        advanceUntilIdle()

        val estado = viewModel.primos.value
        assertTrue(estado is PrimosUi.Listo)
        if (estado is PrimosUi.Listo) {
            assertEquals(168, estado.cantidad)
        }
    }

    @Test
    fun contarPrimos_cancelar_actualizaEstadoCancelado() = runTest {
        fakeRepository.contarPrimosDelayMs = 5000
        viewModel.contar(1000)
        testScheduler.runCurrent()

        assertEquals(PrimosUi.Calculando, viewModel.primos.value)

        viewModel.cancelarPrimos()
        advanceUntilIdle()

        assertEquals(PrimosUi.Cancelado, viewModel.primos.value)
    }

    @Test
    fun cargarUnaPorUna_exito_actualizaExito() = runTest {
        viewModel.cargarUnaPorUna()
        advanceUntilIdle()

        val estado = viewModel.carga.value
        assertTrue(estado is CargaUi.Exito)
        if (estado is CargaUi.Exito) {
            assertEquals("Una por una", estado.modo)
            assertEquals("Ana Torres (ficticia)", estado.perfil)
            assertEquals(3, estado.pedidos)
        }
    }

    @Test
    fun cargarALaVez_exito_actualizaExito() = runTest {
        viewModel.cargarALaVez()
        advanceUntilIdle()

        val estado = viewModel.carga.value
        assertTrue(estado is CargaUi.Exito)
        if (estado is CargaUi.Exito) {
            assertEquals("A la vez", estado.modo)
            assertEquals("Ana Torres (ficticia)", estado.perfil)
            assertEquals(3, estado.pedidos)
        }
    }

    @Test
    fun cargas_conservanResultadosIndependientemente() = runTest {
        viewModel.cargarUnaPorUna()
        advanceUntilIdle()
        assertNotNull(viewModel.resultadoUnaPorUna.value)

        viewModel.cargarALaVez()
        advanceUntilIdle()
        assertNotNull(viewModel.resultadoALaVez.value)
        assertNotNull(viewModel.resultadoUnaPorUna.value)
    }

    @Test
    fun cargarALaVez_conErrorSimulado_actualizaEstadoError() = runTest {
        viewModel.cambiarSimularError(true)
        viewModel.cargarALaVez()
        advanceUntilIdle()

        val estado = viewModel.carga.value
        assertTrue(estado is CargaUi.Error)
        if (estado is CargaUi.Error) {
            assertTrue(estado.mensaje.contains("Sin conexión"))
        }
    }

    @Test
    fun cambiarSimularError_actualizaStateFlow() {
        assertEquals(false, viewModel.simularError.value)
        viewModel.cambiarSimularError(true)
        assertEquals(true, viewModel.simularError.value)
    }
}
