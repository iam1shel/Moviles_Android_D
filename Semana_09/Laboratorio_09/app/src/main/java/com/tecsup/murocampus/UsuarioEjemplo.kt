package com.tecsup.murocampus

import com.tecsup.murocampus.data.UsuarioRepositoryRemote
import com.tecsup.murocampus.data.remote.UsuarioClient
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory
import com.tecsup.murocampus.viewmodel.UsuarioViewModel

// [E] Conexión del ejemplo. La pantalla inicial no lo ejecuta automáticamente.
internal val usuarioFactory = PostsViewModelFactory(UsuarioViewModel::class.java) {
    UsuarioViewModel(UsuarioRepositoryRemote(UsuarioClient.api))
}
