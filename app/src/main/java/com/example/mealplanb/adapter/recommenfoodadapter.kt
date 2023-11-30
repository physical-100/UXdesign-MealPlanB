package com.example.mealplanb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.AddfoodrowBinding
import com.example.mealplanb.databinding.AmountrecommendrowBinding
import com.example.mealplanb.dataclass.food
import com.google.firebase.database.FirebaseDatabase

class recommenfoodadapter(var items:ArrayList<food>,private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<recommenfoodadapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(data: food, holder: ViewHolder)

    }

    private val firebaseDatabase = FirebaseDatabase.getInstance()


    inner class ViewHolder(val binding: AmountrecommendrowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onbind(data: food) {
            binding.result.text = data.foodname
        }

        init {
            binding.result.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], this)
                val position = adapterPosition
                val clickedItem = items[position]
                //해당 음식을 +버튼을 누르면 음식을 추가한다. 데이터베이스에
                //일단 내가 아침 식단을 추가해서 음식을 검색해서 음식을 추가한다면
                //firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능").child("아침//아침이라는 선택 값을 가져와야함.").setValue("음식데이터를 넣어줘야한다.")

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recommenfoodadapter.ViewHolder {
        val view = AmountrecommendrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: recommenfoodadapter.ViewHolder, position: Int) {
        holder.onbind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


}