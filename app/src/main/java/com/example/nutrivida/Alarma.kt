package com.example.nutrivida

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
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

        spinnerHour = findViewById(R.id.spinnerHour)
        spinnerMinute = findViewById(R.id.spinnerMinute)
        spinnerAmPm = findViewById(R.id.spinnerAmPm)
        spinnerDuration = findViewById(R.id.spnDuration)
        spinnerRepit = findViewById(R.id.spnRepit)
        radioGroupDays = findViewById(R.id.radioGroupDays)
        btnSetAlarm = findViewById(R.id.btnSetAlarm)
        btnReturn = findViewById(R.id.btnReturn)

        val hours = (1..12).toList().map { it.toString()}
        val hourAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, hours)
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHour.adapter = hourAdapter

        val minutes = (0..59).toList().map { String.format("%02d" , it) }
        val minuteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, minutes)
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMinute.adapter = minuteAdapter

        val amPm = listOf("AM", "PM")
        val amPmAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, amPm)
        amPmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAmPm.adapter = amPmAdapter

        val duration = listOf("1 minuto", "5 minutos", "10 minutos")
        val durationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, duration)
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDuration.adapter = durationAdapter

        val repit = listOf("5 minutos", "10 minutos", "30 minutos", "60 minutos")
        val repitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, repit)
        repitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRepit.adapter = repitAdapter

        btnSetAlarm.setOnClickListener {
            val selectedHour = spinnerHour.selectedItem.toString().toInt()
            val selectedMinute = spinnerMinute.selectedItem.toString().toInt()
            val selectedAmPm = spinnerAmPm.selectedItem.toString()
            val duration = spinnerDuration.selectedItem.toString().split(" ")[0].toInt()
            val repit = spinnerRepit.selectedItem.toString().split(" ")[0].toInt()

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

        Toast.makeText(this, "Alarma establecida para las $hour:$minute $amPm con una duración de $duration minutos y repeticiones cada $repit minutos", Toast.LENGTH_LONG).show()
    }
}