package com.example.mealplanb.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R
import com.example.mealplanb.fragment.MealhomeFragment

class MealaddAdapter(
    private val meals: MutableList<String>,
    private val mealData: Map<String,MealhomeFragment.onlyCarProFat>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<MealaddAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {

        val meal=meals[position]
        holder.bind(meal)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealOrderTextView: TextView = itemView.findViewById(R.id.mealorder)
        private val carboTextView: TextView = itemView.findViewById(R.id.carbotextView3)
        private val proteinTextView: TextView = itemView.findViewById(R.id.proteintextView3)
        private val fatTextView: TextView = itemView.findViewById(R.id.fattextView3)

        fun bind(meal: String) {
            mealOrderTextView.text = meal

            val mealData = mealData[meal]
            if (mealData != null) {
                carboTextView.text = String.format("%.1f", mealData.carbo)
                proteinTextView.text = String.format("%.1f", mealData.protein)
                fatTextView.text = String.format("%.1f", mealData.fat)
            }

            // 아이템을 클릭할 때 호출할 함수
            itemView.setOnClickListener {
                onItemClick(meal)
            }
        }
    }
}
