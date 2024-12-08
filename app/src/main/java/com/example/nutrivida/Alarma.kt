package com.example.nutrivida

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class Alarma : AppCompatActivity() {

    private lateinit var spinnerHour: Spinner
    private lateinit var spinnerMinute: Spinner
    private lateinit var spinnerAmPm: Spinner
    private lateinit var spinnerDuration: Spinner
    private lateinit var spinnerRepit: Spinner
    private lateinit var radioGroupDays: RadioGroup
    private lateinit var btnSetAlarm: Button
    private lateinit var btnReturn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarma)

        // Inicializando los Spinners y RadioGroup
        spinnerHour = findViewById(R.id.spinnerHour)
        spinnerMinute = findViewById(R.id.spinnerMinute)
        spinnerAmPm = findViewById(R.id.spinnerAmPm)
        spinnerDuration = findViewById(R.id.spnDuration)
        spinnerRepit = findViewById(R.id.spnRepit)
        radioGroupDays = findViewById(R.id.radioGroupDays)
        btnSetAlarm = findViewById(R.id.btnSetAlarm)
        btnReturn = findViewById(R.id.btnReturn)

        // Configurar Spinners (omitir configuración para brevedad)

        btnSetAlarm.setOnClickListener {
            // Obtener la hora, minutos y AM/PM seleccionados
            val selectedHour = spinnerHour.selectedItem.toString().toInt()
            val selectedMinute = spinnerMinute.selectedItem.toString().toInt()
            val selectedAmPm = spinnerAmPm.selectedItem.toString()
            val duration = spinnerDuration.selectedItem.toString().split(" ")[0].toInt()
            val repit = spinnerRepit.selectedItem.toString().split(" ")[0].toInt()

            // Configurar la alarma
            setAlarm(selectedHour, selectedMinute, selectedAmPm, duration, repit)
        }

        btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun setAlarm(hour: Int, minute: Int, amPm: String, duration: Int, repit: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("duration", duration)
            putExtra("repit", repit)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.AM_PM, if (amPm == "AM") Calendar.AM else Calendar.PM)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Exercise Channel"
            val descriptionText = "Channel for exercise notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("exercise_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
