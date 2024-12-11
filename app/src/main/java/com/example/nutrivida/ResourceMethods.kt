package com.example.nutrivida

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.io.File

/**
 *   Esta clase contiene metodos utiles para hacer procesos repetitivos como buscar en SheredPreferences,
 *   asignar la imagen de usuario, etc. Metodos que puedes reutilizar en todas las Activities o clases del proyecto
 *
 *   Basta con instanciar esta clase de la siguiente manera "val resources = ResourceMethods()" y depsues podras usar
 *   los metodos existentes en esta como "saveToSharedPreferences()" de la siquiente manera
 *   "resources.saveToSharedPreferences(contexto, valor)
 *
 *   @param saveToSharedPreferences Este metodo guarda datos en SheredPreferences solo se le tiene que asignar el contexto
 *           de la clase donde se va a usar (Fragment: requireContext(), Class: this) y el valor que se va a guardar. Hay que
 *           tener en cuenta que el tipo de dato a guadar (String, Int, Float) es importante ya que para futuras consultas se
 *           nececsita saber de que tipo es para poder hacer un llamado correcto del dato
 *
 *   @param getFromSharedPreferences Este metodo recupera datos almacenados dentro de SheredPreferences para poder usarlo
 *           se necesita darle el contexto del la clase donde se va a usar (Fragment: requireContext(), Class: this), el nombre
 *           del dato al que se requiere acceder (tag) y un valor por default (defaultValue) en caso de no existir un dato
 *           asignado. Es importante saber el tipo de dato al que se va a recuperar ya que el "defaultValue" tiene que ser del
 *           mismo tipo que el dato original
 *
 *
 *   @author Josh
 *   @since 25/11/24
 */

class ResourceMethods{
    //Metodo para poder guardar datos en SheredPreferences
    fun saveToSharedPreferences(context: Context, tag: String, value: Any) {
        try {
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
        } catch (e: Exception) {
            Toast.makeText(context, "Error saving data in SheredPreferences", Toast.LENGTH_LONG).show()
        }
    }

    //Metodo para poder buscar datos en SheredPreferences
    fun <T> getFromSharedPreferences(context: Context, tag: String, defaultValue: T): T {
        try {
            val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

            //Dependiendo del tipo de dato solicitado es el tipo de busqueda
            return when (defaultValue) {
                is String -> sharedPreferences.getString(tag, defaultValue) as T
                is Int -> sharedPreferences.getInt(tag, defaultValue) as T
                is Float -> sharedPreferences.getFloat(tag, defaultValue) as T
                else -> throw IllegalArgumentException("Unsupported data type")
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error searching data in SheredPreferences", Toast.LENGTH_LONG).show()
            return null as T
        }
    }

    //Metodo para asignar la foto de usuario
    fun loadUserImage(context: Context): Bitmap? {
        try {
            val file = File(context?.filesDir, "userImage.jpg")
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                return bitmap
            } else {
                val drawable = ContextCompat.getDrawable(context, R.drawable.user_image)
                val bitmap = (drawable as BitmapDrawable).bitmap
                return bitmap

            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error loading the image", Toast.LENGTH_LONG).show()
            return null
        }
    }

    companion object {
        private const val CHANNEL_ID = "your_channel_id"
        private const val CHANNEL_NAME = "Your Channel Name"
        private const val CHANNEL_DESCRIPTION = "Your Channel Description"
    }
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}