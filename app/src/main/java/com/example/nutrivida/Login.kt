package com.example.nutrivida

import android.content.Context
import android.content.Intent
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
    private lateinit var banner:TextView

    private var flg: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.txt_email)
        password = findViewById(R.id.txt_password)
        login = findViewById(R.id.btn_login)
        banner = findViewById(R.id.lb_banner)

        //Cambia el texto del boton en funcion si el usuario se va a registrar o loguear
        val bundle = intent.extras
        val type: String = bundle?.getString("flag").toString()
        if(type == "SingIn"){
            login.text = "Registrarse"
            banner.text = "Inicia una vida \nnueva!"
            flg = false
        }

    }

    //Metodo de Validacion de usuario al logear
    fun login (view: View?){
        if(flg){
            try {
                var flag: Boolean = false
                val pref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val dataEmail = pref.getString("email", "")
                val dataPassword = pref.getString("password", "")
                if(dataEmail == email.text.toString() && dataPassword == password.text.toString()){
                    val intent = Intent(this, Menu::class.java)
                    startActivity(intent)
                }
                else{
                    shoWarning("Datos incorrectos")
                }
            }
            catch (ex: Exception){
                Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            createUser()
        }
    }

    //Metodo de creacion de usuarios con los parametros ya obtenidos
    private fun createUser(){
        try{
            if(email.text.isNotEmpty() || password.text.isNotEmpty()){
                val bundle = intent.extras
                //Insercion de los datos del usuario al SheredPreferences
                var pref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                var editor = pref.edit()
                editor.putString("email", email.text.toString())
                editor.putString("password", password.text.toString())
                editor.putString("name", bundle?.getString("name").toString())
                editor.putString("gender", bundle?.getString("gender").toString())
                editor.putInt("age", bundle?.getString("age")?.toIntOrNull() ?: 0)
                editor.putInt("training", bundle?.getString("training")?.toIntOrNull() ?: 11)
                editor.putFloat("height", bundle?.getString("height")?.toFloatOrNull() ?: 40.0f)
                editor.putFloat("weight", bundle?.getString("weight")?.toFloatOrNull() ?: 20.0f)
                editor.putString("goal", bundle?.getString("goal").toString())
                editor.commit() //Guardado de los datos


                //INICIO DEL TAB NAVIGATION
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            }
            else{
                shoWarning("Rellene todos los campos")
            }
        }
        catch (ex: Exception){
            Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo de señalizacion de error al ingresar
    private fun shoWarning(text: String){
        email.setHintTextColor(Color.RED)
        password.setHintTextColor(Color.RED)
        email.hint = text
        password.hint = text
    }
}