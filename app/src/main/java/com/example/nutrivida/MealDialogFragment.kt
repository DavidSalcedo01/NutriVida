package com.example.nutrivida

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class MealDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_ITEM = "item"
        fun newInstance(item: String): MealDialogFragment {
            val args = Bundle()
            args.putString(ARG_ITEM, item)
            val fragment = MealDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.TransparentDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_meal, null)
        val closeButton: ImageButton = view.findViewById(R.id.btn_goBack)
        val acceptButton: Button = view.findViewById(R.id.btn_accept)
        val image: ImageView = view.findViewById(R.id.img_meal)
        val title: TextView = view.findViewById(R.id.lb_dialog_title)
        val description: TextView = view.findViewById(R.id.lb_dialog_description)
        val ingredients: TextView = view.findViewById(R.id.lb_dialog_ingredients)
        val macros: TextView = view.findViewById(R.id.lb_dialog_macros)


        val item = arguments?.getString(ARG_ITEM)
        if(item.equals("Ensalada@2121165466")){
            image.setImageResource(R.drawable.salad)
            title.text = getString(R.string.salad_title)
            description.text = getString(R.string.salad_description)
            ingredients.text = getString(R.string.salad_ingredients)
            macros.text = getString(R.string.salad_macros)
        }
        else if(item.equals("Sandwich@2121165467")){
            image.setImageResource(R.drawable.sandwich)
            title.text = getString(R.string.sandwich_title)
            description.text = getString(R.string.sandwich_description)
            ingredients.text = getString(R.string.sandwich_ingredients)
            macros.text = getString(R.string.sandwich_macros)
        }
        else if(item.equals("Pasta@2121165465")){
            image.setImageResource(R.drawable.pasta)
            title.text = getString(R.string.pasta_title)
            description.text = getString(R.string.pasta_description)
            ingredients.text = getString(R.string.pasta_ingredients)
            macros.text = getString(R.string.pasta_macros)
        }
        else if(item.equals("Huevos@2121165464")){
            image.setImageResource(R.drawable.eggs)
            title.text = getString(R.string.eggs_title)
            description.text = getString(R.string.eggs_description)
            ingredients.text = getString(R.string.eggs_ingredients)
            macros.text = getString(R.string.eggs_macros)
        }


        closeButton.setOnClickListener(){
            dismiss()
        }

        acceptButton.setOnClickListener(){
            dismiss()
        }

        dialog.setContentView(view)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}