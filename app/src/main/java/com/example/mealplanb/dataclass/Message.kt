package com.example.mealplanb.dataclass

enum class MessageType {
    LEFT, RIGHT
}

data class Message(val text: String, val messageType: MessageType,val kcal:String? ,val carb:String?,val protein:String?,val fat:String?)