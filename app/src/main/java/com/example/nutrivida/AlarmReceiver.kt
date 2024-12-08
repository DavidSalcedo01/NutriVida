package com.example.nutrivida

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val duration = intent.getIntExtra("duration", 1)
        val repit = intent.getIntExtra("repit", 5)

        showNotification(context, duration, repit)
    }

    private fun showNotification(context: Context, duration: Int, repit: Int) {
        val stopIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "STOP_ALARM"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val snoozeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "SNOOZE_ALARM"
            putExtra("repit", repit)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "exercise_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarma")
            .setContentText("¡Es hora de tu actividad!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setAutoCancel(true)
            .addAction(R.drawable.ic_stop, "Detener", stopPendingIntent)
            .addAction(R.drawable.ic_snooze, "Posponer", snoozePendingIntent)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
        }
    }
}