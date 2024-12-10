package com.example.nutrivida

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class actividad : Fragment() {

    private var isTimerRunning = false
    private lateinit var countDownTimer: CountDownTimer
    private val totalTime = 1 * 60 * 1000L
    private var timeLeft = totalTime

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val CHANNEL_ID = "exercise_channel"
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_actividad, container, false)
        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = sharedPref.getString("name", "No Name")

        createNotificationChannel()

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
        }

        val nombre: TextView = view.findViewById(R.id.text_UserName)
        nombre.text = "Hola, $username"

        val imbVideo1: ImageButton = view.findViewById(R.id.imb_video1)
        val imbVideo2: ImageButton = view.findViewById(R.id.imb_video2)
        val imbVideo3: ImageButton = view.findViewById(R.id.imb_video3)
        val imbVideo4: ImageButton = view.findViewById(R.id.imb_video4)
        val imbVideo5: ImageButton = view.findViewById(R.id.imb_video5)
        val imbVideo6: ImageButton = view.findViewById(R.id.imb_video6)

        imbVideo1.setOnClickListener { launchVideoActivity("android.resource://${requireContext().packageName}/${R.raw.video1}") }
        imbVideo2.setOnClickListener { launchVideoActivity("android.resource://${requireContext().packageName}/${R.raw.video2}") }
        imbVideo3.setOnClickListener { launchVideoActivity("android.resource://${requireContext().packageName}/${R.raw.video3}") }
        imbVideo4.setOnClickListener { launchVideoActivity("android.resource://${requireContext().packageName}/${R.raw.video4}") }
        imbVideo5.setOnClickListener { launchVideoActivity("android.resource://${requireContext().packageName}/${R.raw.video5}") }
        imbVideo6.setOnClickListener { launchVideoActivity("android.resource://${requireContext().packageName}/${R.raw.video6}") }

        val buttonEjercicio: Button = view.findViewById(R.id.button_ejercicio)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar2)
        val textEjercicioP: TextView = view.findViewById(R.id.text_EjercicioP)
        val ejercicio: TextView = view.findViewById(R.id.text_actividad)

        val buttonAlarm : Button = view.findViewById(R.id.btn_alarma)

        buttonAlarm.setOnClickListener {
            val intent = Intent(context, Alarma::class.java)
            startActivity(intent)
        }

        buttonEjercicio.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
                buttonEjercicio.text = "Continuar"
            } else {
                startTimer(progressBar, textEjercicioP)
                buttonEjercicio.text = "Pausar"
                ejercicio.text = "Hacer ejercicio!"
            }
        }

        return view
    }

    private fun startTimer(progressBar: ProgressBar, textEjercicioP: TextView) {
        countDownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                val progress = updateProgressBar(progressBar, textEjercicioP)
                sharedViewModel.setProgress(progress)
            }

            override fun onFinish() {
                showNotification()
                resetTimer(progressBar, textEjercicioP)
            }
        }.start()
        isTimerRunning = true
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
    }

    private fun resetTimer(progressBar: ProgressBar, textEjercicioP: TextView) {
        timeLeft = totalTime
        updateProgressBar(progressBar, textEjercicioP)
        isTimerRunning = false
    }

    private fun updateProgressBar(progressBar: ProgressBar, textEjercicioP: TextView): Int {
        val progress = ((totalTime - timeLeft).toFloat() / totalTime * 100).toInt()
        progressBar.progress = progress
        textEjercicioP.text = "Progreso: $progress%"
        return progress
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Exercise Channel"
            val descriptionText = "Channel for exercise notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.fitness)
            .setContentTitle("Ejercicio completado")
            .setContentText("¡Has completado tu ejercicio de 25 minutos!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, notification)
        }
    }

    private fun launchVideoActivity(videoUri: String) {
        val intent = Intent(context, Video::class.java).apply {
            putExtra("videoUri", videoUri)
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            } else {
            }
        }
    }
}