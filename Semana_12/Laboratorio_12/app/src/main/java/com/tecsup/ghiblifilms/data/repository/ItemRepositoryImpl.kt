package com.tecsup.ghiblifilms.data.repository

import com.tecsup.ghiblifilms.data.local.ItemDao
import com.tecsup.ghiblifilms.data.mapper.aEntity
import com.tecsup.ghiblifilms.data.mapper.aModelo
import com.tecsup.ghiblifilms.data.remote.ApiService
import com.tecsup.ghiblifilms.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = dao.todos().map { lista -> lista.map { it.aModelo() } }

    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = protegido {
        // El upsert reemplaza la fila completa: sin esto se perderían los favoritos.
        val favoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().map { it.aEntity(esFavorito = it.id in favoritos) }
        dao.guardarTodos(nuevos)
        nuevos.size
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> = protegido {
        api.detalle(id).aModelo(esFavorito = dao.esFavorito(id) ?: false)
    }

    override suspend fun alternarFavorito(id: String) = dao.alternarFavorito(id)

    override suspend fun cantidadGuardada(): Int = dao.contar()

    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()

    override suspend fun limpiarCache() = dao.borrarTodo()

    private inline fun <T> protegido(bloque: () -> T): Result<T> = try {
        Result.success(bloque())
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: SerializationException) {
        Result.failure(e)
    }
}
