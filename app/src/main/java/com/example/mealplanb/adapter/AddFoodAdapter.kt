package com.example.mealplanb.adapter

import android.database.MatrixCursor.RowBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mealplanb.databinding.AddfoodrowBinding
import com.example.mealplanb.dataclass.food

class AddFoodAdapter(val items:ArrayList<food>): RecyclerView.Adapter<AddFoodAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:food, holder:ViewHolder)
    }

    var itemClickListener: OnItemClickListener?=null
    inner class ViewHolder(val binding: AddfoodrowBinding): RecyclerView.ViewHolder(binding.root) {

        fun onbind(data:food) {
            binding.name.text = data.foodname
            binding.brand.text = data.foodbrand
            var quantity:String?= data.foodamount.toString() +"g"
            binding.amount.text =  quantity
            var calory:String? = data.foodcal.toString() +"kcal"
            binding.cal.text = calory
        }

        init {
            binding.addbutton.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFoodAdapter.ViewHolder {
        val view = AddfoodrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddFoodAdapter.ViewHolder, position: Int) {
        holder.onbind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}