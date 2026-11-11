package com.tecsup.ghiblifilms.data.mapper

import com.tecsup.ghiblifilms.data.remote.dto.ItemDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MappersTest {

    private val dto = ItemDto(
        id = "dc2e6bd1-8156-4886-adff-b39e6043af0c",
        title = "Spirited Away",
        originalTitle = "千と千尋の神隠し",
        originalTitleRomanised = "Sen to Chihiro no kamikakushi",
        image = "https://image.tmdb.org/poster.jpg",
        movieBanner = "https://image.tmdb.org/banner.jpg",
        description = "Chihiro entra al mundo de los espíritus.",
        director = "Hayao Miyazaki",
        producer = "Toshio Suzuki",
        releaseDate = "2001",
        runningTime = "124",
        rtScore = "97"
    )

    @Test
    fun `dto a entity convierte el puntaje y respeta el favorito`() {
        val entity = dto.aEntity(esFavorito = true)

        assertEquals(97, entity.puntaje)
        assertEquals("2001", entity.anio)
        assertTrue(entity.esFavorito)
    }

    @Test
    fun `dto a modelo trae los datos del detalle`() {
        val item = dto.aModelo()

        assertEquals(124, item.duracionMin)
        assertEquals("Toshio Suzuki", item.productor)
        assertEquals("千と千尋の神隠し · Sen to Chihiro no kamikakushi", item.tituloOriginal)
        assertEquals("https://image.tmdb.org/banner.jpg", item.bannerUrl)
    }

    @Test
    fun `valores numericos invalidos no rompen el mapeo`() {
        val item = dto.copy(rtScore = "N/A", runningTime = null, originalTitle = null, originalTitleRomanised = "").aModelo()

        assertEquals(0, item.puntaje)
        assertNull(item.duracionMin)
        assertNull(item.tituloOriginal)
    }

    @Test
    fun `entity a modelo no inventa datos del detalle`() {
        val item = dto.aEntity().aModelo()

        assertEquals("Spirited Away", item.titulo)
        assertNull(item.descripcion)
        assertNull(item.bannerUrl)
    }
}
