package com.tecsup.ghiblifilms.data.remote

import com.tecsup.ghiblifilms.data.remote.dto.ItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("films")
    suspend fun listar(): List<ItemDto>

    @GET("films/{id}")
    suspend fun detalle(@Path("id") id: String): ItemDto
}
