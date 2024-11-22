package com.example.nutrivida

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class actividad : Fragment() {

    private var isTimerRunning = false
    private lateinit var countDownTimer: CountDownTimer
    private val totalTime = 1 * 60 * 1000L
    private var timeLeft = totalTime

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_actividad, container, false)
        val nombre: TextView = view.findViewById(R.id.text_UserName)
        val image: ImageView = view.findViewById(R.id.img_userImage)


        //Uso del dato nombre de usuario de Sheredpreferences
        val resources = ResourceMethods()
        val username = resources.getFromSharedPreferences(requireContext(), "name", "Usuario")
        nombre.text = "Hola, $username"

        //Asignacion de la imagen de usuario
        image.setImageBitmap(resources.loadUserImage(requireContext()))


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
                showToast()
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

    private fun showToast() {
        Toast.makeText(requireContext(), "¡Has completado tu ejercicio de 25 minutos!", Toast.LENGTH_LONG).show()
    }

    private fun launchVideoActivity(videoUri: String) {
        val intent = Intent(context, Video::class.java).apply {
            putExtra("videoUri", videoUri)
        }
        startActivity(intent)
    }
}
