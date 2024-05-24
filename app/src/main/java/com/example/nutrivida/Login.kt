package com.example.nutrivida

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.Exception

class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button

    private var flg: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.txt_email)
        password = findViewById(R.id.txt_password)
        login = findViewById(R.id.btn_login)

        val bundle = intent.extras
        val type: String = bundle?.getString("flag").toString()
        if(type == "SingIn"){
            login.text = "Registrarse"
            flg = false
        }

    }

    fun login (view: View?){
        if(flg){
            try {
                var flag: Boolean = false
                for (item in Users.user){
                    if(item.email == email.text.toString() && item.password == password.text.toString()){
                        Toast.makeText(this,"Datos correctos", Toast.LENGTH_SHORT).show();
                        flag = true
                    }
                }
                if (!flag){
                    email.setText("")
                    password.setText("")
                    shoWarning("Datos incorrectos")
                }
            }
            catch (ex: Exception){
                Toast.makeText(this,"Datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            createUser()
        }
    }

    private fun createUser(){
        try{
            if(!email.text.isEmpty() || !password.text.isEmpty()){
                val bundle = intent.extras
                val newUser = Users(
                    email.text.toString(),
                    password.text.toString(),
                    bundle?.getString("name").toString(),
                    bundle?.getString("gender").toString(),
                    bundle?.getString("age")?.toIntOrNull() ?: 0,
                    bundle?.getString("training")?.toIntOrNull() ?: 11,
                    bundle?.getString("heigth")?.toFloatOrNull() ?: 40.0f,
                    bundle?.getString("weigth")?.toFloatOrNull() ?: 20.0f,
                    bundle?.getString("goal").toString()
                )
                Users.user.add(newUser)
                Toast.makeText(this,"Usuario registrado", Toast.LENGTH_SHORT).show();
            }
            else{
                shoWarning("Rellene todos los campos")
            }
        }
        catch (ex: Exception){
            Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show();
        }
    }

    private fun shoWarning(text: String){
        email.setHintTextColor(Color.RED)
        password.setHintTextColor(Color.RED)
        email.hint = text
        password.hint = text
    }
}