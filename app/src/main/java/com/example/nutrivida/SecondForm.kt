package com.example.nutrivida

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

class SecondForm : AppCompatActivity() {
    private lateinit var heightamount: TextView
    private lateinit var height: SeekBar
    private lateinit var weightamount: TextView
    private lateinit var weight: SeekBar
    private lateinit var imgdim:ImageView
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


        height.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress > 10){
                    amountH = (progress * 2.5).toFloat()
                    heightamount.text = "$amountH cm"
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        weight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress > 5){
                    amountW = (progress * 4).toFloat()
                    weightamount.text = "$amountW kg"
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

    }

    fun back(view: View?){
        finish()
    }

    fun finish(view: View?){
        val bundle = intent.extras
        val intent = Intent(this, Login::class.java)
        intent.putExtra("flag", "SingIn")
        intent.putExtra("heigth",amountH.toString())
        intent.putExtra("weigth",amountW.toString())
        intent.putExtra("goal",goal.selectedItem.toString())
        intent.putExtra("name", bundle?.getString("name"))
        intent.putExtra("age", bundle?.getString("age"))
        intent.putExtra("training", bundle?.getString("training"))
        intent.putExtra("gender", bundle?.getString("gender"))
        startActivity(intent)
    }
}