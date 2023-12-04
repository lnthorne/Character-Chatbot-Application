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
            append(". Your goal is to ")
            append(character?.goal ?: "unknown")
            append(". ")
            append(character?.backgroundContext ?: "No background context provided.")
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
        return CompletionRequest(
            messages = combinedMessageData,
        )
    }
}