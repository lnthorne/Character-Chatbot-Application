package com.example.character_chatbot_application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.repositorys.GPTRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ChatViewModel(
    private val database: AppDatabase,
    private val gptRepository: GPTRepository
): ViewModel() {
    private val _messages = MutableLiveData<MutableList<ChatMessage>>()
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    private var characterDescription: String? = null
    private var isInitialMessage = true
    var userId: Int? = 0

    init {
        _messages.value = mutableListOf()
    }

    fun loadCharacterData(userId: Int) {
        viewModelScope.launch {
            val characters = database.characterDao.getCharactersByUserId(userId).first()
            characterDescription = characters.firstOrNull()?.description
        }
    }

    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            // Add user message to LiveData
            addMessageToLiveData(ChatMessage(messageText, MessageType.USER))

            val messageHistory = _messages.value.orEmpty()
            val character = userId?.let { database.characterDao.getCharactersByUserId(it).firstOrNull() }
            try {
                val response = gptRepository.getCompletion(character, messageHistory)
                response?.let {
                    addMessageToLiveData(ChatMessage(it.content, MessageType.BOT))
                }
            } catch (e: Exception) {
                logError("GPT API call failed: ${e.message}")
            }
        }
    }


    private fun addMessageToLiveData(message: ChatMessage) {
        val currentMessages = _messages.value ?: mutableListOf()
        currentMessages.add(message)
        _messages.postValue(currentMessages)
    }

    private fun logError(message: String) {
        println("Error: $message")
    }

    private fun getApiKey(): String {
        return BuildConfig.GPT_KEY
    }
}
