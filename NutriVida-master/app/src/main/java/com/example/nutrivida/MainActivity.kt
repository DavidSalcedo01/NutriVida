package com.example.nutrivida

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

    }

    fun LogIn(view: View?){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun SingIn(view: View?){
        val intent = Intent(this, SingIn::class.java)
        startActivity(intent)
    }
}