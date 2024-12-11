package com.example.nutrivida

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val ARG_PARAM1 = "param1"
const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Perfil.newInstance] factory method to
 * create an instance of this fragment.
 */

class Perfil : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        val profile_name = view.findViewById<TextView>(R.id.profile_name)
        val weight_value = view.findViewById<TextView>(R.id.weight_value)
        val height_value = view.findViewById<TextView>(R.id.height_value)
        val age_value = view.findViewById<TextView>(R.id.age_value)
        val image: ImageView = view.findViewById(R.id.img_userImage)
        val editProfileTextView = view.findViewById<TextView>(R.id.edit_profile)


        val resources = ResourceMethods()
        val username = resources.getFromSharedPreferences(requireContext(), "name", "Usuario")
        db.collection("users").document(username)  // Use username as the document ID
            .get()  // Fetch the document
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document found, retrieve the data
                    val name = documentSnapshot.getString("name")
                    val age = documentSnapshot.getString("age")?.toInt() ?: 0
                    val height = documentSnapshot.getString("height")?.toFloat() ?: 0.0f
                    val weight = documentSnapshot.getString("weight")?.toFloat() ?: 0.0f

                    // Update your UI with the retrieved data
                    profile_name.text = name
                    age_value.text = age.toString()
                    height_value.text = height.toString()
                    weight_value.text = "$weight kg"

                } else {
                    // Handle the case where the document doesn't exist
                    Toast.makeText(requireContext(), "User not found in Firestore", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Toast.makeText(requireContext(), "Failed to load profile data", Toast.LENGTH_SHORT).show()
            }
        image.setImageBitmap(resources.loadUserImage(requireContext()))


        editProfileTextView.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Perfil.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Perfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}