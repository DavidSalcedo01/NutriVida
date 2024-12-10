package com.example.nutrivida

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        val duration = intent.getIntExtra("duration", 1) * 60 * 1000 // Convert minutes to milliseconds
        val repit = intent.getIntExtra("repit", 5)

        showNotification(context, duration, repit)
        playAlarmSound(context)

        // Stop the alarm after the specified duration
        Handler(Looper.getMainLooper()).postDelayed({
            stopAlarm(context)
        }, duration.toLong())
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
            .setSmallIcon(R.drawable.baseline_access_alarm_24)
            .setContentTitle("Alarma")
            .setContentText("¡Es hora de tu actividad!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_stop, "Detener", stopPendingIntent)
            .addAction(R.drawable.ic_snooze, "Posponer", snoozePendingIntent)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1, notification)
        }
    }

    private fun playAlarmSound(context: Context) {
        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(context, alarmUri).apply {
            isLooping = true
            start()
        }
    }

    private fun stopAlarm(context: Context) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1)
    }
}