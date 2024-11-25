package com.example.nutrivida

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //App Check
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )

        val pref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        //Determinamos si el usuario ya esta logeado
        if(pref.contains("email") && pref.contains("password")){
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

    }

    fun LogIn(view: View?){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun SingIn(view: View?){
        val intent = Intent(this, SignIn::class.java)
        startActivity(intent)
    }
}