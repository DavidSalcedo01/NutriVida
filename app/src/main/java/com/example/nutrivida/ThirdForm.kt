package com.example.nutrivida

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class ThirdForm : AppCompatActivity() {
    private lateinit var conditions: MultiAutoCompleteTextView
    private lateinit var waist: EditText
    private lateinit var hip: EditText
    private lateinit var arm: EditText
    private lateinit var leg: EditText
    private lateinit var vegan: CheckBox
    private lateinit var vegetarian: CheckBox
    private lateinit var carnivorous: CheckBox
    private lateinit var pescatarian: CheckBox
    private lateinit var flexitarian: CheckBox
    private lateinit var glutenFree: CheckBox
    private lateinit var keto: CheckBox
    private lateinit var supplements: MultiAutoCompleteTextView
    private var preferenceSummary = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third_form)
        conditions = findViewById(R.id.mult_conditions)
        waist = findViewById(R.id.txt_waist)
        hip = findViewById(R.id.txt_hip)
        arm = findViewById(R.id.txt_arm)
        leg = findViewById(R.id.txt_leg)
        vegan = findViewById(R.id.chb_vegan)
        vegetarian = findViewById(R.id.chb_vegetarian)
        carnivorous = findViewById(R.id.chb_carnivorous)
        pescatarian = findViewById(R.id.chb_pescatarian)
        flexitarian = findViewById(R.id.chb_flexitarian)
        glutenFree = findViewById(R.id.chb_gluten_free)
        keto = findViewById(R.id.chb_keto)
        supplements = findViewById(R.id.mult_supplements)

        //Asignacion de valores a las lista de condiciones
        val conditions_items =  resources.getStringArray(R.array.conditions)
        val multiCond_adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, conditions_items)
        multiCond_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        conditions.setAdapter(multiCond_adapter)
        conditions.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        //Asignacion de valores a las lista de suplementos
        val supplements_items =  resources.getStringArray(R.array.supplements)
        val multiSupp_adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, supplements_items)
        multiSupp_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        supplements.setAdapter(multiSupp_adapter)
        supplements.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

    }

    //Metodo del boton regresar
    fun back(view: View?){
        finish()
    }


    //Metodo de inicio del "Login" en modo de Registrar nuevo usuario
    fun finish(view: View?){
        if(waist.getText().isNotEmpty() || hip.getText().isNotEmpty() || arm.getText().isNotEmpty() || leg.getText().isNotEmpty()){
            val waisM = waist.text.toString().toFloat()
            val hipM = hip.text.toString().toFloat()
            val armM = arm.text.toString().toFloat()
            val legM = leg.text.toString().toFloat()
            getSelectedCheckBoxes()

            if(waisM in 40.0f..100.0f && hipM in 80.0f..120.0f && armM in 20.0f..40.0f && legM in 30.0f..60.0f){

                val resources = ResourceMethods()
                val intent = Intent(this, Login::class.java)
                resources.saveToSharedPreferences(this, "conditions", conditions.text.toString())
                resources.saveToSharedPreferences(this, "waistMeasure", waist.text.toString().toFloat())
                resources.saveToSharedPreferences(this, "hipMeasure", hip.text.toString().toFloat())
                resources.saveToSharedPreferences(this, "armMeasure", arm.text.toString().toFloat())
                resources.saveToSharedPreferences(this, "legMeasure", leg.text.toString().toFloat())
                resources.saveToSharedPreferences(this, "preferences", preferenceSummary)
                resources.saveToSharedPreferences(this, "supplements", supplements.text.toString())
                intent.putExtra("flag", "SingIn") //Flag que nos ayuda a determinar si el usuario es nuevo
                startActivity(intent)

            }else{
                waist.setText("")
                hip.setText("")
                arm.setText("")
                leg.setText("")
                waist.setHintTextColor(Color.RED)
                hip.setHintTextColor(Color.RED)
                arm.setHintTextColor(Color.RED)
                leg.setHintTextColor(Color.RED)
                waist.hint = "Cantidad invalida"
                hip.hint = "Cantidad invalida"
                arm.hint = "Cantidad invalida"
                leg.hint = "Cantidad invalida"

            }
        }
        else{
            waist.setHintTextColor(Color.RED)
            hip.setHintTextColor(Color.RED)
            arm.setHintTextColor(Color.RED)
            leg.setHintTextColor(Color.RED)
            waist.hint = "Cantidad invalida"
            hip.hint = "Cantidad invalida"
            arm.hint = "Cantidad invalida"
            leg.hint = "Cantidad invalida"
        }


    }

    private fun getSelectedCheckBoxes() {
        val summary = StringBuilder()

        if (vegetarian.isChecked) {
            summary.append("vegetariano, ")
        }
        if (vegan.isChecked) {
            summary.append("vegano, ")
        }
        if (carnivorous.isChecked) {
            summary.append("carnivoro, ")
        }
        if (pescatarian.isChecked) {
            summary.append("pescetariano, ")
        }
        if (flexitarian.isChecked) {
            summary.append("flexitariano, ")
        }
        if (glutenFree.isChecked) {
            summary.append("sin gluten, ")
        }
        if (keto.isChecked) {
            summary.append("keto, ")
        }

        // Quita la última coma y espacio si hay al menos un elemento
        if (summary.isNotEmpty()) {
            preferenceSummary = summary.substring(0, summary.length - 2)
        } else {
            preferenceSummary = "Ninguna preferencia seleccionada"
        }
    }

}
