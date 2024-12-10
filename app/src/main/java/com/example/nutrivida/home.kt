package com.example.nutrivida

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class home : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = sharedPref.getString("name", "No Name")
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val progressBarA: ProgressBar = view.findViewById(R.id.progressBar_a)
        val progressBarS: ProgressBar = view.findViewById(R.id.progressBar_s)
        val progressBarC: ProgressBar = view.findViewById(R.id.progressBar_c)
        val progressBarV: ProgressBar = view.findViewById(R.id.progressBar_v)
        val progressBarP: ProgressBar = view.findViewById(R.id.progressBar_principal)
        val progressBarPasos: ProgressBar = view.findViewById(R.id.progressBar_pasos)
        val progreso: TextView = view.findViewById(R.id.text_pro)
        val addRegis: Button = view.findViewById(R.id.btn_progreso)

        val nombre: TextView = view.findViewById(R.id.text_UserName)
        val pasos: TextView = view.findViewById(R.id.text_pasos)

        nombre.text = "Hola, $username"

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sensorManager.registerListener(stepListener, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Toast.makeText(context, "Sensor de pasos no disponible", Toast.LENGTH_SHORT).show()
        }

        addRegis.setOnClickListener {
            val regisDialog = Dialog(requireContext())
            regisDialog.setContentView(R.layout.add_progreso)

            val agua = regisDialog.findViewById<EditText>(R.id.txt_agua)
            val vasos: TextView = view.findViewById(R.id.text_numCup)

            val sue = regisDialog.findViewById<EditText>(R.id.txt_sueno)
            val horas: TextView = view.findViewById(R.id.text_h)

            val cal = regisDialog.findViewById<EditText>(R.id.txt_calorias)
            val calorias: TextView = view.findViewById(R.id.text_c)

            regisDialog.findViewById<Button>(R.id.btn_reg).setOnClickListener {

                val aguaInt = agua.text.toString().toInt()
                val suenoInt = sue.text.toString().toInt()
                val calInt = cal.text.toString().toInt()

                val calR = (calInt / 2000.0) * 100

                vasos.text = agua.text.toString()
                horas.text = "Horas: " + sue.text.toString()
                calorias.text = calR.toString()

                progressBarS.progress = suenoInt
                progressBarV.progress = aguaInt
                progressBarC.progress = calR.toInt()

                updatePrincipalProgress(progressBarP, progressBarA, progressBarS, progressBarC, progressBarV, progreso)

                regisDialog.dismiss()
            }
            regisDialog.show()
        }

        sharedViewModel.progress.observe(viewLifecycleOwner, Observer { progress ->
            progressBarA.progress = progress
            updatePrincipalProgress(progressBarP, progressBarA, progressBarS, progressBarC, progressBarV, progreso)
        })

        return view
    }

    private fun updatePrincipalProgress(
        progressBarP: ProgressBar,
        progressBarA: ProgressBar,
        progressBarS: ProgressBar,
        progressBarC: ProgressBar,
        progressBarV: ProgressBar,
        progreso: TextView
    ) {
        val totalProgress = ((progressBarA.progress + progressBarS.progress + progressBarC.progress + progressBarV.progress) - 23) / 2
        progressBarP.progress = totalProgress
        progreso.text = totalProgress.toString()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private val stepListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                totalSteps = event.values[0]
                val currentSteps = totalSteps - previousTotalSteps
                updateStepCount(currentSteps.toInt())
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    private fun updateStepCount(currentSteps: Int) {
        val pasos: TextView = view?.findViewById(R.id.text_pasos) ?: return
        val progressBarPasos: ProgressBar = view?.findViewById(R.id.progressBar_pasos) ?: return

        pasos.text = "Pasos: $currentSteps"
        progressBarPasos.progress = (currentSteps * 100 / 8000)
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("previousTotalSteps", previousTotalSteps)
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        previousTotalSteps = sharedPref.getFloat("previousTotalSteps", 0f)
    }
}