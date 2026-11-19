package com.tecsup.centrotareas.data

class TareasRepositoryLocal : TareasRepository {

    override suspend fun cargarPerfil(): String {
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(
        simularError: Boolean
    ): List<String> {
        return listOf(
            "Pedido 101",
            "Pedido 102",
            "Pedido 103"
        )
    }

    override suspend fun contarPrimos(hasta: Int): Int {
        return 0
    }
}
