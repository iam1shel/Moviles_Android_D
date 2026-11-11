package com.tecsup.ghiblifilms.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.ghiblifilms.MainActivity
import com.tecsup.ghiblifilms.R

class NotificadorAndroid(private val context: Context) : Notificador {

    init {
        crearCanal()
    }

    private fun crearCanal() {
        val canal = NotificationChannel(
            CANAL_ID,
            "Sincronización",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Avisa cuando se guardan películas en el celular" }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    @SuppressLint("MissingPermission")
    override fun mostrar(titulo: String, texto: String) {
        if (!tienePermiso()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val alTocar = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setContentIntent(alTocar)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIFICACION_ID, notificacion)
    }

    private fun tienePermiso(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    private companion object {
        const val CANAL_ID = "sincronizacion"
        const val NOTIFICACION_ID = 1
    }
}
