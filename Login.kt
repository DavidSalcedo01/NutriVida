package com.example.nutrivida

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var banner: TextView
    private lateinit var passwordWarning: TextView
    private lateinit var googleSignInButton: ImageButton
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private var flg: Boolean = true

    companion object {
        private const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.txt_email)
        password = findViewById(R.id.txt_password)
        login = findViewById(R.id.btn_login)
        banner = findViewById(R.id.lb_banner)
        passwordWarning = findViewById(R.id.lb_passwordWarning)
        googleSignInButton = findViewById(R.id.btn_google)

        // Inicializar FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Configurar botón de Google Sign-In
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Cambiar texto del botón dependiendo de la acción (registro o login)
        val bundle = intent.extras
        val type: String = bundle?.getString("flag").toString()
        if (type == "SingIn") {
            login.text = "Registrarse"
            banner.text = "Inicia una vida \nnueva!"
            flg = false
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In falló: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this, "Autenticación fallida.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Métodos previos para autenticación por email y password
    fun login(view: View?) {
        if (flg) {
            try {
                val pref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val dataEmail = pref.getString("email", "")
                val dataPassword = pref.getString("password", "")
                if (dataEmail == email.text.toString() && dataPassword == password.text.toString()) {
                    val intent = Intent(this, Menu::class.java)
                    startActivity(intent)
                } else {
                    email.setText("")
                    password.setText("")
                    shoWarning("Datos incorrectos", "", "both")
                }
            } catch (ex: Exception) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        } else {
            createUser()
        }
    }

    private fun createUser() {
        try {
            if (email.text.isNotEmpty() || password.text.isNotEmpty()) {
                if (emailValidation(email.text.toString())) {
                    if (passwordValidation(password.text.toString())) {
                        val resources = ResourceMethods()
                        resources.saveToSharedPreferences(this, "email", email.text.toString())
                        resources.saveToSharedPreferences(this, "password", password.text.toString())

                        val intent = Intent(this, Menu::class.java)
                        startActivity(intent)
                    } else {
                        shoWarning(
                            "Contraseña no válida",
                            "La contraseña debe contener mayúsculas, minúsculas, números y símbolos",
                            "password"
                        )
                    }
                } else {
                    shoWarning("Email no válido", "", "email")
                }
            } else {
                shoWarning("Rellene todos los campos", "", "both")
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shoWarning(text: String, info: String, type: String) {
        if (type.equals("email")) {
            email.setText("")
            email.setHintTextColor(Color.RED)
            email.hint = text
        } else if (type.equals("password")) {
            password.setText("")
            password.setHintTextColor(Color.RED)
            password.hint = text
            passwordWarning.text = info
        } else {
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
}
