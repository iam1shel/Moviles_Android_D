package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(private val repository: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _queryBusqueda = MutableStateFlow("")
    val queryBusqueda: StateFlow<String> = _queryBusqueda.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private val _crearState = MutableStateFlow<UiState<Post>?>(null)
    val crearState: StateFlow<UiState<Post>?> = _crearState.asStateFlow()

    private var currentDetalleId: Int? = null
    private var cargarJob: Job? = null
    private var postsServidor: List<Post> = emptyList()

    init {
        cargar()
    }

    fun cargar() {
        cargarJob?.cancel()
        cargarJob = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val result = if (userId == null) {
                repository.listar()
            } else {
                repository.porUsuario(userId)
            }
            _lista.value = result.fold(
                onSuccess = { posts ->
                    postsServidor = posts
                    val query = _queryBusqueda.value
                    val filtrados = if (query.isBlank()) posts else posts.filter { it.title.contains(query, ignoreCase = true) }
                    UiState.Exito(filtrados)
                },
                onFailure = { UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun actualizarBusqueda(query: String) {
        _queryBusqueda.value = query
        val filtrados = if (query.isBlank()) {
            postsServidor
        } else {
            postsServidor.filter { it.title.contains(query, ignoreCase = true) }
        }
        _lista.value = UiState.Exito(filtrados)
    }

    fun abrirDetalle(id: Int) {
        currentDetalleId = id
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            val postResult = repository.detalle(id)
            val comResult = repository.comentarios(id)
            if (postResult.isSuccess && comResult.isSuccess) {
                _detalle.value = UiState.Exito(
                    DetalleUi(
                        postResult.getOrThrow(),
                        comResult.getOrThrow()
                    )
                )
            } else {
                val error = postResult.exceptionOrNull()
                    ?: comResult.exceptionOrNull()
                    ?: Exception("Error desconocido")
                _detalle.value = UiState.Error(mensajeDe(error))
            }
        }
    }

    fun reintentarDetalle() {
        currentDetalleId?.let { abrirDetalle(it) }
    }

    fun crear(titulo: String, texto: String) {
        viewModelScope.launch {
            _crearState.value = UiState.Cargando
            val result = repository.crear(NuevoPost(userId = 1, title = titulo, body = texto))
            _crearState.value = result.fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun resetCrear() {
        _crearState.value = null
    }
}
