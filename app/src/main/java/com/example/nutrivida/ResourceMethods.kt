package com.example.nutrivida

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import java.io.File

class ResourceMethods {

    //Metodo para poder guardar datos en SheredPreferences
    fun saveToSharedPreferences(context: Context, tag: String, value: Any){
        val pref = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var editor = pref.edit()

        //Dependiendo del tipo de dato es como se va a guardar
        when (value) {
            is String -> editor.putString(tag, value)
            is Int -> editor.putInt(tag, value)
            is Float -> editor.putFloat(tag, value)
            else -> throw IllegalArgumentException("Unsupported data type")
        }

        editor.apply()
    }

    //Metodo para poder buscar datos en SheredPreferences
    fun <T> getFromSharedPreferences(context: Context, tag: String, defaultValue: T): T {
        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

        //Dependiendo del tipo de dato solicitado es el tipo de busqueda
        return when (defaultValue) {
            is String -> sharedPreferences.getString(tag, defaultValue) as T
            is Int -> sharedPreferences.getInt(tag, defaultValue) as T
            is Float -> sharedPreferences.getFloat(tag, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported data type")
        }
    }

    //Metodo para asignar la foto de usuario
    fun loadUserImage(context: Context): Bitmap? {
        val file = File(context?.filesDir, "userImage.jpg")
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            return bitmap
        } else {
            val drawable = ContextCompat.getDrawable(context, R.drawable.user_image)
            val bitmap = (drawable as BitmapDrawable).bitmap
            return bitmap

        }
    }

}