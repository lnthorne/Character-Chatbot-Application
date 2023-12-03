package com.example.character_chatbot_application

data class ChatMessage(
    val message: String,
    val type: MessageType
)

enum class MessageType {
    USER, BOT
}

