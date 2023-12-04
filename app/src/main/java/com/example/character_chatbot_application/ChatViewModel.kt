package com.example.character_chatbot_application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.character_chatbot_application.Util.Globals
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.repositorys.GPTRepository
import com.example.character_chatbot_application.repositorys.StoryRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val storyRepository: StoryRepository,
    private val gptRepository: GPTRepository
): ViewModel() {
    private val _messages = MutableLiveData<MutableList<ChatMessage>>()
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    private val _character: LiveData<Character> = storyRepository.getCharacterByCharacterId(Globals.CHARACTER_ID)
    val character: LiveData<Character> = _character

    init {
        _messages.value = mutableListOf()
    }

    fun sendMessage(messageText: String, character: Character) {
        viewModelScope.launch {
            // Add user message to LiveData
            addMessageToLiveData(ChatMessage(messageText, MessageType.USER))

            val messageHistory = _messages.value.orEmpty()
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
