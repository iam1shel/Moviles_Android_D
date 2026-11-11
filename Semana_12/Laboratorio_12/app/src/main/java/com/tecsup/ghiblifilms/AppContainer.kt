package com.tecsup.ghiblifilms

import android.content.Context
import com.google.firebase.FirebaseApp
import com.tecsup.ghiblifilms.data.auth.AuthRepository
import com.tecsup.ghiblifilms.data.auth.AuthSinConfigurar
import com.tecsup.ghiblifilms.data.auth.FirebaseAuthRepository
import com.tecsup.ghiblifilms.data.local.AppDatabase
import com.tecsup.ghiblifilms.data.remote.RetrofitClient
import com.tecsup.ghiblifilms.data.repository.ItemRepository
import com.tecsup.ghiblifilms.data.repository.ItemRepositoryImpl
import com.tecsup.ghiblifilms.notification.Notificador
import com.tecsup.ghiblifilms.notification.NotificadorAndroid

class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    private val baseDatos = AppDatabase.crear(appContext)

    val itemRepository: ItemRepository = ItemRepositoryImpl(RetrofitClient.api, baseDatos.itemDao())

    val authRepository: AuthRepository =
        if (FirebaseApp.getApps(appContext).isEmpty()) AuthSinConfigurar else FirebaseAuthRepository()

    val notificador: Notificador = NotificadorAndroid(appContext)
}
