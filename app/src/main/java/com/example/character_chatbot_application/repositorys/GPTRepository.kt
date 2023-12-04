package com.example.character_chatbot_application.repositorys

import android.util.Log
import com.example.character_chatbot_application.ChatMessage
import com.example.character_chatbot_application.MessageType
import com.example.character_chatbot_application.Util.CompletionRequest
import com.example.character_chatbot_application.Util.GPTService
import com.example.character_chatbot_application.Util.MessageContent
import com.example.character_chatbot_application.data.models.Character



class GPTRepository(private val gptService: GPTService) {

    suspend fun getCompletion(character: Character?, messageHistory: List<ChatMessage>): MessageContent? {
        val prompt = constructPrompt(character, messageHistory)
        try {
            val response = gptService.callGptApi(prompt)

            if (response.isSuccessful) {
                return response.body()?.choices?.firstOrNull()?.message
            } else {
                val errorString = response.errorBody()?.string()
                throw Exception("API error: $errorString")
            }
        } catch (e: Exception) {
            throw Exception("API error: $e")
        }

    }

    private fun constructPrompt(
        character: Character?,
        messageHistory: List<ChatMessage>
    ): CompletionRequest {
        Log.i("GPT", character?.name.toString())

        val systemMessageContent = buildString {
            append("You are ")
            append(character?.name ?: "an unnamed character")
            append(", ")
            append(character?.description ?: "with no specific description")
            append(". Here is some background information that the user has provided: ")
            append(character?.backgroundContext ?: "unknown")
            append(". You must carry out a deep story with the user, playing only the role of your character")
//            append("You MUST use JSON for your API response.")
        }


        val systemMessage = MessageContent(
            role = "system",
            content = systemMessageContent
        )

        val messageContent = messageHistory.map { message ->
            MessageContent(
                role = if (message.type == MessageType.USER) "user" else "assistant",
                content = message.message
            )
        }


        val combinedMessageData = listOf(systemMessage) + messageContent
        Log.i("Test", combinedMessageData.toString())
        return CompletionRequest(
            messages = combinedMessageData,
        )
    }
}