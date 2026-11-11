package com.tecsup.ghiblifilms

import android.app.Application

class GhibliApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
