package com.example.nutrivida

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.nfc.FormatException
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.FirebaseFirestore

class SecondForm : AppCompatActivity() {
    private lateinit var heightamount: TextView
    private lateinit var height: SeekBar
    private lateinit var weightamount: TextView
    private lateinit var weight: SeekBar
    private lateinit var imgdim: ImageView
    private lateinit var goal: Spinner
    private lateinit var sleep: EditText

    private var amountH: Float = 170f
    private var amountW: Float = 60f
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_secondform)
        heightamount = findViewById(R.id.lb_height)
        height = findViewById(R.id.sbr_height)
        weightamount = findViewById(R.id.lb_weight)
        weight = findViewById(R.id.sbr_weight)
        imgdim = findViewById(R.id.img_dimensions)
        goal = findViewById(R.id.spn_goal)
        sleep = findViewById(R.id.txt_sleep)




        //Asignacion de valores a las lista de metas
        val goals_items =  resources.getStringArray(R.array.goals)
        val spinner_adapter = ArrayAdapter(this, R.layout.spinner_item, goals_items)
        spinner_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        goal.adapter = spinner_adapter


        //Evento donde se recupera la informacion del SeekBar "heigth"
        height.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress > 10){
                    //Se multiplica el valor por 2.5 para llevar una proporcion coerente
                    amountH = (progress * 2.5).toFloat()
                    heightamount.text = "$amountH cm"
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //Evento donde se recupera la informacion del SeekBar "weight"
        weight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress > 5){
                    //Se multiplica el valor por 4 para llevar una proporcion coerente
                    amountW = (progress * 4).toFloat()
                    weightamount.text = "$amountW kg"
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    //Metodo del boton regresar
    fun back(view: View?){
        finish()
    }

    //Metodo de validacion de datos ingresados y inicio del "ThirdForm"
    fun nextForm_two(view: View?){
        try{
            if(sleep.getText().toString().isNotEmpty()){
                val slp = sleep.text.toString().toInt()
                if(slp in 3..23){
                    val resources = ResourceMethods()
                    val username = resources.getFromSharedPreferences(this, "name", "Usuario")
                    // Data for sub-collection
                    val userDetails = hashMapOf(
                        "height" to amountH,
                        "weight" to amountW,
                        "goal" to goal.selectedItem.toString(),
                        "sleep" to slp
                    )

                    db.collection("users").document(username).collection("details")
                        .add(userDetails)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Firestore", "Details added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error adding details", e)
                        }
                    val intent = Intent(this, ThirdForm::class.java)
                    resources.saveToSharedPreferences(this, "height", amountH)
                    resources.saveToSharedPreferences(this, "weight", amountW)
                    resources.saveToSharedPreferences(this, "goal", goal.selectedItem.toString())
                    resources.saveToSharedPreferences(this, "sleep", slp)
                    startActivity(intent)
                }
                else{
                    sleep.setText("")
                    sleep.setHintTextColor(Color.RED)
                    sleep.hint = "Cantidad invalida"
                }
            }
            else{
                sleep.setHintTextColor(Color.RED)
                sleep.hint = "Rellene el campo"
            }
        }catch (ex: Exception){
            Toast.makeText(this,"No se pudo procesar los datos", Toast.LENGTH_SHORT).show();
        }
    }
}