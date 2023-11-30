package com.example.mealplanb.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R
import com.example.mealplanb.dataclass.food

class MealaddAdapter(
private val meals: MutableList<String>,
private val onItemClick: (String) -> Unit,
private val onDeleteClick: (String) -> Unit
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
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(meal: String) {
            mealOrderTextView.text = meal

            // 아이템을 클릭할 때 호출할 함수
            itemView.setOnClickListener {
                onItemClick(meal)
            }
            itemView.setOnLongClickListener {
                // 롱 프레스 시 삭제 버튼을 표시하도록 설정
                deleteButton.visibility = View.VISIBLE
                true
            }
            deleteButton.setOnClickListener {
                onDeleteClick(meal)
            }
        }
    }
}
