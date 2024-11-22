package com.example.nutrivida

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [calorias.newInstance] factory method to
 * create an instance of this fragment.
 */
class calorias : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var lst_meals: ListView
    private lateinit var lb_calories: TextView
    private lateinit var prb_calories: ProgressBar
    private lateinit var lb_carbohydrates: TextView
    private lateinit var prb_carbohydrates: ProgressBar
    private lateinit var lb_protein: TextView
    private lateinit var prb_protein: ProgressBar
    private lateinit var lb_fat: TextView
    private lateinit var prb_fat: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calorias, container, false)
        val lb_day = view.findViewById<TextView>(R.id.lb_day)

        lb_calories = view.findViewById(R.id.lb_calories)
        prb_calories = view.findViewById(R.id.prb_calories)
        lb_carbohydrates = view.findViewById(R.id.lb_carbohydrates)
        prb_carbohydrates = view.findViewById(R.id.prb_carbohydrates)
        lb_protein = view.findViewById(R.id.lb_protein)
        prb_protein = view.findViewById(R.id.prb_protein)
        lb_fat = view.findViewById(R.id.lb_fat)
        prb_fat = view.findViewById(R.id.prb_fat)
        val image: ImageView = view.findViewById(R.id.img_userImage)



        //Uso del dato nombre de usuario de Sheredpreferences
        val resources = ResourceMethods()
        val username = resources.getFromSharedPreferences(requireContext(), "name", "Usuario")
        lb_day.text = "Hola, $username\n${date()}"

        //Asignacion de la imagen de usuario
        image.setImageBitmap(resources.loadUserImage(requireContext()))

        //Asignacion de datos a la lista de comidas sugeridas
        lst_meals = view.findViewById(R.id.lst_meals)
        val items = listOf("Ensalada@2121165466", "Sandwich@2121165467", "Pasta@2121165465", "Huevos@2121165464")
        val adapter = CustomAdapter(requireContext(), items)
        lst_meals.adapter = adapter

        lst_meals.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            showItemDialog(items[position])
            updateProgress(items[position])
        }

        return view
    }

    //Muestra la ventana de comida segun sea el caso
    private fun showItemDialog(item: String) {
        val dialog = MealDialogFragment.newInstance(item)
        dialog.show(parentFragmentManager, "ItemDialogFragment")
    }

    //Metodo para actualizar los progresos de alimentacion del usuario
    private fun updateProgress(item: String){
        if (item.equals("Ensalada@2121165466")) {
            prb_calories.progress += 9
            prb_carbohydrates.progress += 3
            prb_protein.progress += 3
            prb_fat.progress += 0
        } else if (item.equals("Sandwich@2121165467")) {
            prb_calories.progress += 22
            prb_carbohydrates.progress += 15
            prb_protein.progress += 20
            prb_fat.progress += 6
        } else if (item.equals("Pasta@2121165465")) {
            prb_calories.progress += 13
            prb_carbohydrates.progress += 21
            prb_protein.progress += 2
            prb_fat.progress += 3
        } else if (item.equals("Huevos@2121165464")) {
            prb_calories.progress += 15
            prb_carbohydrates.progress += 10
            prb_protein.progress += 31
            prb_fat.progress += 8
        }

        lb_calories.text = prb_calories.progress.toString()
        val carb = prb_calories.progress.toString()
        lb_carbohydrates.text = "$carb%"
        val prot = prb_carbohydrates.progress.toString()
        lb_protein.text = "$prot%"
        val fat = prb_fat.progress.toString()
        lb_fat.text = "$fat%"
    }

    private fun date(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMM", Locale("es", "ES"))
        return dateFormat.format(calendar.time)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment calorias.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            calorias().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}