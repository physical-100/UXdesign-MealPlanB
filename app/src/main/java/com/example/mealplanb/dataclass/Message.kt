package com.example.mealplanb.dataclass

enum class MessageType {
    LEFT, RIGHT,LEFT_2
}

data class Message(val text: String, val messageType: MessageType,val kcal:String? ,val carb:String?,val protein:String?,val fat:String?,val text2:String?)