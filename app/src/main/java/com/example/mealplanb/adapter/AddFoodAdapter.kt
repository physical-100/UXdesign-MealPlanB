package com.example.mealplanb.adapter

import android.database.MatrixCursor.RowBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mealplanb.databinding.AddfoodrowBinding
import com.example.mealplanb.dataclass.food
import com.google.firebase.database.FirebaseDatabase


class AddFoodAdapter(val items:ArrayList<food>): RecyclerView.Adapter<AddFoodAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data:food, holder:ViewHolder)
        fun OnaddBtnClick(data:food, holder:RecyclerView.ViewHolder)
        
    }
    private val firebaseDatabase = FirebaseDatabase.getInstance()


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
            binding.clickfood.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], this)
                val position =adapterPosition
                val clickedItem = items[position]
                //해당 음식을 +버튼을 누르면 음식을 추가한다. 데이터베이스에
                //일단 내가 아침 식단을 추가해서 음식을 검색해서 음식을 추가한다면
                //firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능").child("아침//아침이라는 선택 값을 가져와야함.").setValue("음식데이터를 넣어줘야한다.")

            }
//            binding.addbutton.setOnClickListener {
//                itemClickListener?.OnaddBtnClick(items[adapterPosition], this)
//                //해당 음식을 +버튼을 누르면 음식을 추가한다. 데이터베이스에
//                //일단 내가 아침 식단을 추가해서 음식을 검색해서 음식을 추가한다면
//                //firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능").child("아침//아침이라는 선택 값을 가져와야함.").setValue("음식데이터를 넣어줘야한다.")
//            }
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
    fun setData(newData: ArrayList<food>) {
        items.addAll(newData)
        notifyDataSetChanged()
    }



}