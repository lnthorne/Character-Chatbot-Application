package com.example.character_chatbot_application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.repositorys.GPTRepository

class ChatViewModelFactory(private val database: AppDatabase,
                           private val gptRepository: GPTRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(database, gptRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
