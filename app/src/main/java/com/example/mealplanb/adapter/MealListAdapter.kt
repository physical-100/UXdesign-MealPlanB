package com.example.mealplanb.adapter

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.example.mealplanb.MealData
import com.example.mealplanb.R
import com.example.mealplanb.dataclass.food

class MealListAdapter(private val context: Context, private val mealList: List<MealData>,private val onitemClick: (MealData) -> Unit, private val onDeleteClick: (MealData) -> Unit) :
    ArrayAdapter<MealData>(context, 0, mealList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.detailmealrow, parent, false
            )
        }

        val currentMeal = getItem(position)

        val mealNameTextView = listItemView?.findViewById<TextView>(R.id.foodname)
        mealNameTextView?.text=currentMeal?.foodname

//        val mealBrandTextView=listItemView?.findViewById<TextView>(R.id.foodbrand)
//        mealBrandTextView?.text=currentMeal?.foodbrand

        val mealKcalTextView=listItemView?.findViewById<TextView>(R.id.food_Kcal)
        mealKcalTextView?.text=String.format("%.1f",currentMeal?.foodcal)+"Kcal"

        val mealamountTextView=listItemView?.findViewById<TextView>(R.id.foodamout)
        mealamountTextView?.text=String.format("%.1f",currentMeal?.foodamount)+"g"

        val deleteButton = listItemView?.findViewById<ImageButton>(R.id.deleteitem)
        val clickitem = listItemView?.findViewById<LinearLayout>(R.id.meal_detail_item)
        clickitem?.setOnClickListener {
            val clickeditem = getItem(position)
            onitemClick(clickeditem!!)
            notifyDataSetChanged()
        }

        // 삭제 버튼 클릭 시 onDeleteClick 호출
        deleteButton?.setOnClickListener {
            val mealToDelete = getItem(position)
            onDeleteClick(mealToDelete!!)
            notifyDataSetChanged()

        }



        // Set other TextViews as needed

        return listItemView!!
    }
}