package com.example.nutrivida

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Load existing data into input fields
        loadProfileData()

        // Save profile data when the user clicks the Save button
        findViewById<Button>(R.id.btn_save_profile).setOnClickListener {
            saveProfileData()
        }
    }

    private fun loadProfileData() {
        // Load data from SharedPreferences
        findViewById<EditText>(R.id.edit_name).setText(sharedPreferencesHelper.getString("name", ""))
    }

    private fun saveProfileData() {
        val resources = ResourceMethods()
        // Retrieve updated data from input fields

        val name = findViewById<EditText>(R.id.edit_name).text.toString()
        val age = findViewById<EditText>(R.id.edit_age).text.toString()
        val training = findViewById<EditText>(R.id.edit_training).text.toString()
        val gender = findViewById<EditText>(R.id.edit_gender).text.toString()

        // Save data to SharedPreferences
        resources.saveToSharedPreferences(this, "name", name)

        // Save data to Firestore
        val userMap = hashMapOf(
            "name" to name,
            "age" to age,
            "training" to training,
            "gender" to gender
        )
        db.collection("users").document("users")
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                finish() // Go back to the previous activity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update profile!", Toast.LENGTH_SHORT).show()
            }
    }
}