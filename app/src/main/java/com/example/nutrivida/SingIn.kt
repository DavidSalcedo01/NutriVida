package com.example.nutrivida

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.nfc.FormatException
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import java.io.File

class SingIn : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST_CODE = 1
    private lateinit var maleGender: Chip
    private lateinit var femaleGender: Chip
    private lateinit var noneGender: Chip
    private lateinit var image: ImageView
    private lateinit var name: EditText
    private lateinit var age: EditText
    private lateinit var traning: SeekBar
    private lateinit var frequency: TextView
    private lateinit var btnnext: Button

    private var traningf: Int = 0
    private var gender: String = ""
    var selectedImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_singin)
        maleGender = findViewById(R.id.chp_maleGender)
        femaleGender = findViewById(R.id.chp_femaleGender)
        noneGender = findViewById(R.id.chp_noneGender)
        image = findViewById(R.id.img_userImage)
        name = findViewById(R.id.txt_name)
        age = findViewById(R.id.txt_age)
        traning = findViewById(R.id.sbr_trainingf)
        frequency = findViewById(R.id.lb_trainingf)
        btnnext = findViewById(R.id.btn_next)

        //Evento cuando el elemento "chip - Masculino" es precionado
        maleGender.setOnClickListener {
            resetInputs()
            femaleGender.chipBackgroundColor = getColorStateList(R.color.white)
            noneGender.chipBackgroundColor = getColorStateList(R.color.white)

            if(gender.isEmpty() || gender != "Maculino"){
                maleGender.chipBackgroundColor = getColorStateList(R.color.primary)
                maleGender.setTextColor(Color.WHITE)
                gender = "Masculino"
            }
            else{
                maleGender.chipBackgroundColor = getColorStateList(R.color.white)
                maleGender.setTextColor(Color.BLACK)
                gender = ""
            }

        }



        //Evento cuando el elemento "chip - Femenino" es precionado
        femaleGender.setOnClickListener {
            resetInputs()
            maleGender.chipBackgroundColor = getColorStateList(R.color.white)
            noneGender.chipBackgroundColor = getColorStateList(R.color.white)

            if(gender.isEmpty() || gender != "Femenino"){
                femaleGender.chipBackgroundColor = getColorStateList(R.color.primary)
                femaleGender.setTextColor(Color.WHITE)
                gender = "Femenino"
            }
            else{
                femaleGender.chipBackgroundColor = getColorStateList(R.color.white)
                femaleGender.setTextColor(Color.BLACK)
                gender = ""
            }
        }

        //Evento cuando el elemento "chip - Ninguno" es precionado
        noneGender.setOnClickListener {
            resetInputs()
            maleGender.chipBackgroundColor = getColorStateList(R.color.white)
            femaleGender.chipBackgroundColor = getColorStateList(R.color.white)

            if (gender.isEmpty() || gender != "Ninguno") {
                noneGender.chipBackgroundColor = getColorStateList(R.color.primary)
                noneGender.setTextColor(Color.WHITE)
                gender = "Ninguno"
            } else {
                noneGender.chipBackgroundColor = getColorStateList(R.color.white)
                noneGender.setTextColor(Color.BLACK)
                gender = ""
            }
        }

        //Evento del SeekBar "Actividad fisica" recupera el valor seleccionado
        traning.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress == 6){
                    frequency.text = "Entrena 7 veces por semana"
                }
                else{
                    val rng = progress + 2
                    frequency.text = "Entrena de $progress-$rng veces por semana"
                }
                traningf = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    //Metodo del boton regresar
    fun back(view: View?){
        finish()
    }

    //Metodo de validacion de datos ingresados y inicio del "SecondForm"
     fun nextForm(view: View?){
        if(!gender.isEmpty()){
            if (name.getText().toString().isEmpty() || age.getText().toString().isEmpty()){
                Toast.makeText(this,"Rellene todos los campos", Toast.LENGTH_SHORT).show();
                name.setHintTextColor(Color.RED)
                name.hint = "Rellene el campo"
                age.setHintTextColor(Color.RED)
                age.hint = "Rellene el campo"
            }
            else{
                try {
                    val Age = age.text.toString().toInt()

                    if (Age in 11..79){
                        //Envio de datos recuperados a SheredPreferences
                        val intent = Intent(this, SecondForm::class.java)
                        val resources = ResourceMethods()
                        resources.saveToSharedPreferences(this, "name", name.text.toString())
                        resources.saveToSharedPreferences(this, "age", Age)
                        resources.saveToSharedPreferences(this, "training", traningf.toString())
                        resources.saveToSharedPreferences(this, "gender", gender)
                        startActivity(intent)
                    }
                    else{
                        age.setText("")
                        age.setHintTextColor(Color.RED)
                        age.hint = "Edad no valida"
                    }
                }catch (ex: FormatException){
                    Toast.makeText(this,"No se pudo procesar los datos", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this,"Tiene que escoger un genero", Toast.LENGTH_SHORT).show();
            maleGender.setChipStrokeColorResource(R.color.warning)
            femaleGender.setChipStrokeColorResource(R.color.warning)
            noneGender.setChipStrokeColorResource(R.color.warning)
            maleGender.setTextColor(Color.RED)
            femaleGender.setTextColor(Color.RED)
            noneGender.setTextColor(Color.RED)
        }
    }

    //Metodo de reseteo de colores de los "chip" en caso de error
    private fun resetInputs(){
        femaleGender.setChipStrokeColorResource(R.color.text_gray)
        maleGender.setChipStrokeColorResource(R.color.text_gray)
        noneGender.setChipStrokeColorResource(R.color.text_gray)
        maleGender.setTextColor(Color.BLACK)
        noneGender.setTextColor(Color.BLACK)
        femaleGender.setTextColor(Color.BLACK)
    }

    //Metodo para agregar una imagen de usuario
    fun addIMage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" // Solo imágenes
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)

    }


    //Metodo para que el usuario escoja una imagen de su galeria
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                val file = File(this.filesDir, "userImage.jpg")
                val outputStream = file.outputStream()
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()

                // Mostrar la imagen en el ImageView
                image.setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }


}