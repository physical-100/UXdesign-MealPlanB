package com.example.mealplanb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
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
                R.layout.meal_list_item, parent, false
            )
        }

        val currentMeal = getItem(position)

        val mealNameTextView = listItemView?.findViewById<TextView>(R.id.foodname)
        mealNameTextView?.text=currentMeal?.foodname

        val mealBrandTextView=listItemView?.findViewById<TextView>(R.id.foodbrand)
        mealBrandTextView?.text=currentMeal?.foodbrand

        val mealKcalTextView=listItemView?.findViewById<TextView>(R.id.food_kcal)
        mealKcalTextView?.text=String.format("%.1f",currentMeal?.foodcal)+"Kcal"

        val mealamountTextView=listItemView?.findViewById<TextView>(R.id.foodamount)
        mealamountTextView?.text=String.format("%.1f",currentMeal?.foodamount)+"g"


        // Set other TextViews as needed

        return listItemView!!
    }
}