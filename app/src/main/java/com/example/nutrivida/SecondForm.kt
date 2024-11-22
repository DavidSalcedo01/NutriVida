package com.example.nutrivida

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SecondForm : AppCompatActivity() {
    private lateinit var heightamount: TextView
    private lateinit var height: SeekBar
    private lateinit var weightamount: TextView
    private lateinit var weight: SeekBar
    private lateinit var imgdim: ImageView
    private lateinit var goal: Spinner

    private var amountH: Float = 170f
    private var amountW: Float = 60f

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

        //Asignacion de valores a las lista de metas
        val items =  resources.getStringArray(R.array.goals)

        val adapter = ArrayAdapter(this, R.layout.spinner_item, items)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        goal.adapter = adapter


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

    //Metodo de inicio del "Login" en modo de Registrar nuevo usuario
    fun finish(view: View?){
        val resources = ResourceMethods()
        resources.saveToSharedPreferences(this, "height", amountH)
        resources.saveToSharedPreferences(this, "weight", amountW)
        resources.saveToSharedPreferences(this, "goal", goal.selectedItem.toString())

        val intent = Intent(this, Login::class.java)
        //Envio de todos los datos recuperados para ser almacenados
        intent.putExtra("flag", "SingIn") //Flag que nos ayuda a determinar si el usuario es nuevo
        startActivity(intent)
    }
}