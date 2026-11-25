package com.tecsup.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal object UsuarioClient {
    private val json = Json { ignoreUnknownKeys = true }

    val api: UsuarioApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(UsuarioApi::class.java)
    }
}
