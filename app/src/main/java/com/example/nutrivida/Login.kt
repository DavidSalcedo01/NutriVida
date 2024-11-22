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
import java.lang.Exception
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var banner:TextView
    private lateinit var passwordWarning:TextView

    private var flg: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.txt_email)
        password = findViewById(R.id.txt_password)
        login = findViewById(R.id.btn_login)
        banner = findViewById(R.id.lb_banner)
        passwordWarning = findViewById(R.id.lb_passwordWarning)

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
                    email.setText("")
                    password.setText("")
                    shoWarning("Datos incorrectos","", "both")
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
                if(emailValidation(email.text.toString())){
                    if(passwordValidation(password.text.toString())){
                        //Insercion de los datos del usuario al SheredPreferences
                        val resources = ResourceMethods()
                        resources.saveToSharedPreferences(this, "email",  email.text.toString())
                        resources.saveToSharedPreferences(this, "password",  password.text.toString())

                        //INICIO DEL TAB NAVIGATION
                        val intent = Intent(this, Menu::class.java)
                        startActivity(intent)
                    }
                    else{
                        shoWarning("Contraseña no valida","La contraseña debe contener mayusculas, minusculas, numeros y simbolos", "password")
                    }
                }
                else{
                    shoWarning("Email no valido","", "email")
                }
            }
            else{
                shoWarning("Rellene todos los campos","", "both")
            }
        }
        catch (ex: Exception){
            Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo de señalizacion de error al ingresar
    private fun shoWarning(text: String, info: String, type: String){
        if(type.equals("email")){
            email.setText("")
            email.setHintTextColor(Color.RED)
            email.hint = text
        }
        else if (type.equals("password")){
            password.setText("")
            password.setHintTextColor(Color.RED)
            password.hint = text
            passwordWarning.text = info

        }
        else{
            email.setHintTextColor(Color.RED)
            password.setHintTextColor(Color.RED)
            email.hint = text
            password.hint = text
        }
    }

    private fun emailValidation(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }
    private fun passwordValidation(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!?])(?=\\S+$).{8,}$"
        val passwordMatcher = Regex(passwordPattern)
        return passwordMatcher.matches(password)
    }

    //Metodo de validacion de login por Google         NO IMPLEMENTADO AUN!
    fun validateLogin(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val db: FirebaseFirestore = Firebase.firestore
        db.collection("Users")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val user = documents.documents[0]
                    val storedPassword = user.getString("Password")
                    if (storedPassword == password) {
                        onSuccess() }
                    else {
                        onFailure("Contraseña incorrecta")
                    }
                }
                else {
                    onFailure("Usuario no encontrado")
                }
            }
            .addOnFailureListener { exception ->
                onFailure("Error al validar el usuario: ${exception.message}")
            }
    }
}