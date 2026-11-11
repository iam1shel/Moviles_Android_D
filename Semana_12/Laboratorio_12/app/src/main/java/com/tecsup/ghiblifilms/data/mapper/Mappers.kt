package com.tecsup.ghiblifilms.data.mapper

import com.tecsup.ghiblifilms.data.local.ItemEntity
import com.tecsup.ghiblifilms.data.remote.dto.ItemDto
import com.tecsup.ghiblifilms.domain.model.Item

fun ItemDto.aEntity(esFavorito: Boolean = false) = ItemEntity(
    id = id,
    titulo = title,
    director = director,
    anio = releaseDate,
    puntaje = rtScore?.toIntOrNull() ?: 0,
    imagenUrl = image,
    esFavorito = esFavorito
)

fun ItemEntity.aModelo() = Item(
    id = id,
    titulo = titulo,
    director = director,
    anio = anio,
    puntaje = puntaje,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)

fun ItemDto.aModelo(esFavorito: Boolean = false) = Item(
    id = id,
    titulo = title,
    director = director,
    anio = releaseDate,
    puntaje = rtScore?.toIntOrNull() ?: 0,
    imagenUrl = image,
    esFavorito = esFavorito,
    tituloOriginal = listOfNotNull(originalTitle, originalTitleRomanised)
        .filter { it.isNotBlank() }
        .joinToString(" · ")
        .ifBlank { null },
    productor = producer,
    duracionMin = runningTime?.toIntOrNull(),
    descripcion = description,
    bannerUrl = movieBanner
)
