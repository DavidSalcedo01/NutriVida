package com.example.nutrivida

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "STOP_ALARM" -> {
                stopAlarm(context)
            }
            "SNOOZE_ALARM" -> {
                val repit = intent.getIntExtra("repit", 5)
                snoozeAlarm(context, repit)
            }
        }
    }

    private fun stopAlarm(context: Context) {
        AlarmReceiver.mediaPlayer?.stop()
        AlarmReceiver.mediaPlayer?.release()
        AlarmReceiver.mediaPlayer = null

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1)

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "La alarma ha sido detenida", Toast.LENGTH_LONG).show()
        }
    }

    private fun snoozeAlarm(context: Context, repit: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val snoozeTime = System.currentTimeMillis() + repit * 60 * 1000
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, snoozeTime, pendingIntent)

        AlarmReceiver.mediaPlayer?.stop()
        AlarmReceiver.mediaPlayer?.release()
        AlarmReceiver.mediaPlayer = null

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1)

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "La alarma sonará nuevamente dentro de $repit minutos", Toast.LENGTH_LONG).show()
        }
    }
}