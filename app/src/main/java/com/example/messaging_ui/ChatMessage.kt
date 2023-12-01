package com.example.messaging_ui

data class ChatMessage(
    val message: String,
    val type: MessageType
)

enum class MessageType {
    USER, BOT
}

