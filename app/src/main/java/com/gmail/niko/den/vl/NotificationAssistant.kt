package com.gmail.niko.den.vl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationAssistant {
    companion object {
        fun createNotificationChannel(
            context: Context,
            importance: Int,
            appName: String,
            description: String,
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    "${context.packageName}-${appName}",
                    appName,
                    importance
                ).also {
                    it.description = description
                }

                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

        fun createNotification(
            context: Context,
            intent: Intent,
            message: String?,
        ) {
            val contactId = intent.getStringExtra("CONTACT_ID")
            val getActivityIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("CONTACT_ID", contactId)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                getActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(
                context,
                "${context.packageName}-${context.getString(R.string.app_name)}"
            ).apply {
                setSmallIcon(R.drawable.android_logo_icon)
                setContentTitle(context.getString(R.string.app_name))
                setContentText(message)
                setStyle(NotificationCompat.BigTextStyle().bigText(message))
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT
            }

            NotificationManagerCompat.from(context).notify(
                contactId.hashCode(),
                notificationBuilder.build()
            )
        }
    }
}