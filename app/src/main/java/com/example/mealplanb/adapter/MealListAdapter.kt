package com.example.mealplanb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mealplanb.MealData
import com.example.mealplanb.R
import com.example.mealplanb.dataclass.food

class MealListAdapter(private val context: Context, private val mealList: List<MealData>) :
    ArrayAdapter<MealData>(context, 0, mealList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.fragment_meal_detail, parent, false
            )
        }

        val currentMeal = getItem(position)

        //val mealNameTextView = listItemView?.findViewById<TextView>(R.id.mealNameTextView)
        //mealNameTextView?.text = currentMeal?.foodname



        // Set other TextViews as needed

        return listItemView!!
    }
}