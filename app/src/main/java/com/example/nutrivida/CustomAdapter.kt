package com.example.nutrivida

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val context: Context, private val dataMeals: List<String>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return dataMeals.size
    }
    override fun getItem(position: Int): Any {
        return dataMeals[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //Salad: 2121165466 Pasta: 2121165465 Sandwich: 2121165467 Eggs: 2121165464


        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item, parent, false)
            holder = ViewHolder()
            holder.imgMeal = view.findViewById(R.id.img_meal) as ImageView
            holder.nameMeal = view.findViewById(R.id.lb_meal) as TextView
            holder.calories = view.findViewById(R.id.lb_meal_calories) as TextView
            holder.arowImage = view.findViewById(R.id.img_arrow) as ImageView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val item = getItem(position) as String
        val parts = item.split("@")
        val name = parts[0]
        val image = parts[1]

        holder.nameMeal.text = name
        holder.calories.text = "230 KCAL"
        if(image.equals("2121165466")){
            holder.imgMeal.setImageResource(R.drawable.salad)
        }
        else if(image.equals("2121165465")){
            holder.imgMeal.setImageResource(R.drawable.sandwich)
        }
        else if(image.equals("2121165467")){
            holder.imgMeal.setImageResource(R.drawable.pasta)
        }
        else if(image.equals("2121165464")){
            holder.imgMeal.setImageResource(R.drawable.eggs)
        }
        //holder.imgMeal.setImageResource(image.toInt())

        return view
    }

    private class ViewHolder {
        lateinit var imgMeal: ImageView
        lateinit var nameMeal: TextView
        lateinit var calories: TextView
        lateinit var arowImage: ImageView
    }
}