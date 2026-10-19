package com.tecsup.huellitasvet

import android.app.Application
import com.tecsup.huellitasvet.data.local.DatosIniciales
import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class VetApp : Application() {
    lateinit var db: VetDatabase
        private set
    lateinit var repo: VetRepository
        private set
    lateinit var prefs: PreferenciasStore
        private set

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        db = VetDatabase.obtener(this)
        repo = VetRepositoryRoom(db)
        prefs = PreferenciasStore(this)
        scope.launch { DatosIniciales.sembrar(db) }
    }
}
