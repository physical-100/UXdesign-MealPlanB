package com.example.mealplanb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.databinding.AddfoodrowBinding
import com.example.mealplanb.databinding.FastRecordRowBinding
import com.example.mealplanb.dataclass.fast_record
import com.example.mealplanb.dataclass.food
import com.google.firebase.database.FirebaseDatabase

class FastrecordAdapter(val items:ArrayList<fast_record>): RecyclerView.Adapter<FastrecordAdapter.ViewHolder>() {


        interface OnItemClickListener{
            fun OnItemClick(data: fast_record, holder:ViewHolder)

        }
        var itemClickListener: OnItemClickListener?=null
        inner class ViewHolder(val binding: FastRecordRowBinding): RecyclerView.ViewHolder(binding.root) {

            fun onbind(data: fast_record) {
                binding.fastTime.text = data.fasttime
                binding.startTime.text = data.starttime
                binding.endTime.text = data.endtime

            }

            init {
                binding.fastRecord.setOnClickListener {
                    itemClickListener?.OnItemClick(items[adapterPosition], this)
                    val position =adapterPosition
                    val clickedItem = items[position]

                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastrecordAdapter.ViewHolder {
            val view = FastRecordRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: FastrecordAdapter.ViewHolder, position: Int) {
            holder.onbind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }