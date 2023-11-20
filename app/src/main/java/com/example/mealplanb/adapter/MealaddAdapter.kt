package com.example.mealplanb.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R

class MealaddAdapter(
    private val meals: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<MealaddAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.bind(meal)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealOrderTextView: TextView = itemView.findViewById(R.id.mealorder)

        fun bind(meal: String) {
            mealOrderTextView.text = meal

            // 아이템을 클릭할 때 호출할 함수
            itemView.setOnClickListener {
                Log.i("meal",meal)
                onItemClick(meal)
            }
        }
    }

}
