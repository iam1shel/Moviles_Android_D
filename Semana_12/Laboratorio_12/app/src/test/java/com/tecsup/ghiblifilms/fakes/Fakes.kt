package com.tecsup.ghiblifilms.fakes

import com.tecsup.ghiblifilms.data.auth.AuthRepository
import com.tecsup.ghiblifilms.data.repository.ItemRepository
import com.tecsup.ghiblifilms.domain.model.Item
import com.tecsup.ghiblifilms.domain.model.Sesion
import com.tecsup.ghiblifilms.notification.Notificador
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

fun pelicula(id: String, favorito: Boolean = false) = Item(
    id = id,
    titulo = "Película $id",
    director = "Hayao Miyazaki",
    anio = "2001",
    puntaje = 97,
    imagenUrl = null,
    esFavorito = favorito
)

/** Simula Retrofit + Room: refrescar() reemplaza lo guardado conservando favoritos. */
class FakeItemRepository(
    guardados: List<Item> = emptyList(),
    var resultadoRefresco: Result<List<Item>> = Result.success(emptyList())
) : ItemRepository {

    private val datos = MutableStateFlow(guardados)
    var llamadasRefrescar = 0
        private set

    override val items: Flow<List<Item>> = datos

    override val favoritos: Flow<List<Item>> = datos.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> {
        llamadasRefrescar++
        return resultadoRefresco.map { nuevos ->
            val favoritos = datos.value.filter { it.esFavorito }.map { it.id }.toSet()
            datos.value = nuevos.map { it.copy(esFavorito = it.id in favoritos) }
            nuevos.size
        }
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> =
        datos.value.firstOrNull { it.id == id }?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException(id))

    override suspend fun alternarFavorito(id: String) = datos.update { lista ->
        lista.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
    }

    override suspend fun cantidadGuardada(): Int = datos.value.size

    override suspend fun cantidadFavoritos(): Int = datos.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        datos.value = emptyList()
    }
}

class FakeNotificador : Notificador {
    val mostrados = mutableListOf<Pair<String, String>>()

    override fun mostrar(titulo: String, texto: String) {
        mostrados += titulo to texto
    }
}

class FakeAuthRepository(
    private val sesionGuardada: Sesion? = null,
    private val resultadoLogin: Result<Sesion> = Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
) : AuthRepository {

    var llamadasLogin = 0
        private set
    var sesionCerrada = false
        private set

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> {
        llamadasLogin++
        return resultadoLogin
    }

    override suspend fun sesionActual(): Sesion? = sesionGuardada

    override fun cerrarSesion() {
        sesionCerrada = true
    }
}
