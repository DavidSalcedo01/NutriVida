package com.example.nutrivida

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Perfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class Perfil : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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


        //Busqueda en SheredPreferences para el llenado de datos
        val resources = ResourceMethods()
        val username: String = resources.getFromSharedPreferences(requireContext(), "name", "Usuario")
        val height: Float = resources.getFromSharedPreferences(requireContext(), "height", 0.0f)
        val weight: Float = resources.getFromSharedPreferences(requireContext(), "weight", 0.0f)
        val age: Int = resources.getFromSharedPreferences(requireContext(), "age", 0)
        image.setImageBitmap(resources.loadUserImage(requireContext()))

        profile_name.text = username
        weight_value.text = "$weight kg"
        height_value.text = height.toString()
        age_value.text = age.toString()

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