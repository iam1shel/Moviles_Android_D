package com.tecsup.centrotareas

import com.tecsup.centrotareas.data.TareasRepositoryLocal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TareasRepositoryTest {

    private lateinit var repository: TareasRepositoryLocal

    @Before
    fun setUp() {
        repository = TareasRepositoryLocal()
    }

    @Test
    fun contarPrimos_hasta1000_retorna168() = runTest {
        val resultado = repository.contarPrimos(1000)
        assertEquals(168, resultado)
    }

    @Test
    fun contarPrimos_hasta500000_retorna41538() = runTest {
        val resultado = repository.contarPrimos(500000)
        assertEquals(41538, resultado)
    }

    @Test
    fun cargarPerfil_retornaNombreCorrecto() = runTest {
        val perfil = repository.cargarPerfil()
        assertEquals("Ana Torres (ficticia)", perfil)
    }

    @Test
    fun cargarPedidos_sinError_retornaListaPedidos() = runTest {
        val pedidos = repository.cargarPedidos(simularError = false)
        assertEquals(3, pedidos.size)
        assertEquals("Pedido 101", pedidos[0])
    }

    @Test(expected = IOException::class)
    fun cargarPedidos_conError_lanzaIOException() = runTest {
        repository.cargarPedidos(simularError = true)
    }

    @Test
    fun observarProgreso_emiteValoresHasta1() = runTest {
        val valores = repository.observarProgreso().toList()
        assertEquals(100, valores.size)
        assertEquals(1.0f, valores.last(), 0.001f)
    }
}
