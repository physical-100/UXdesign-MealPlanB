package com.example.mealplanb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R
import com.example.mealplanb.dataclass.Message
import com.example.mealplanb.dataclass.MessageType

class ChatAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MessageType.LEFT.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_left, parent, false)
                LeftViewHolder(view)
            }
            MessageType.RIGHT.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_right, parent, false)
                RightViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            MessageType.LEFT.ordinal -> {
                val leftViewHolder = holder as LeftViewHolder
                // 메시지를 leftTextView에 설정
                leftViewHolder.leftText.text = message.text
                // 이미지 설정 (이미지가 있다면 보이게, 없다면 숨기게 처리)
                if (message.kcal!=null) {
                    leftViewHolder.leftcal.text = message.kcal
                    leftViewHolder.leftcarb.text = message.carb
                    leftViewHolder.leftprotein.text = message.protein
                    leftViewHolder.leftfat.text =  message.fat

                } else {
                    leftViewHolder.leftcal.visibility = View.GONE
                    leftViewHolder.leftdetail.visibility = View.GONE
                }
            }
            MessageType.RIGHT.ordinal -> {
                // 오른쪽에 표시할 내용을 설정하세요
                val rightViewHolder = holder as RightViewHolder
                val rightTextView = rightViewHolder.itemView.findViewById<TextView>(R.id.righttextview)
                // 메시지를 오른쪽 TextView에 설정
                rightTextView.text = message.text
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return messages[position].messageType.ordinal
    }
    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leftText: TextView = itemView.findViewById(R.id.textview1)
        val leftcal: TextView = itemView.findViewById(R.id.kcal)
        val leftdetail: LinearLayout = itemView.findViewById(R.id.detail)
        val leftcarb: TextView = itemView.findViewById(R.id.remain_carb)
        val leftprotein: TextView = itemView.findViewById(R.id.remain_protein)
        val leftfat: TextView = itemView.findViewById(R.id.remain_fat)

    }
    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

