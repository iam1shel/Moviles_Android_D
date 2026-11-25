package com.tecsup.murocampus.data

import com.tecsup.murocampus.data.remote.PostApi
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.CancellationException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {
    private inline fun <T> safeCall(block: () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        }

    override suspend fun listar(): Result<List<Post>> = safeCall { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> = safeCall { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> = safeCall { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> = safeCall { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = safeCall { api.crear(nuevo) }
}
