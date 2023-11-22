package com.example.character_chatbot_application.repositorys

import android.util.Log
import com.example.character_chatbot_application.Util.CompletionRequest
import com.example.character_chatbot_application.Util.GPTService
import com.example.character_chatbot_application.Util.MessageContent
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.data.models.Message



class GPTRepository(private val gptService: GPTService) {

    suspend fun getCompletion(character: Character?, messageHistory: List<Message>): MessageContent? {
        val prompt = constructPrompt(character, messageHistory)
        Log.i("prompt", prompt.toString())

        try {
            val response = gptService.callGptApi(prompt)

            if (response.isSuccessful) {
                Log.i("GPT_Repository_getCompletion", "Success: ${response.body()}")
                return response.body()?.choices?.firstOrNull()?.message
            } else {
                val errorString = response.errorBody()?.string()
                Log.e("GPT_Repository_getCompletion", "API error: $errorString")
                throw Exception("API error: $errorString")
            }
        } catch (e: Exception) {
            Log.e("GPT_Repository_getCompletion", "Exception: ${e.localizedMessage}", e)
            return null
        }

    }
    private fun constructPrompt(
        character: Character?,
        messageHistory: List<Message>
    ): CompletionRequest {
         val systemMessage = MessageContent(
             role = "system",
             content = "You are ${character?.name}, ${character?.description}" +
                     "your goal is to ${character?.goal}. ${character?.backgroundContext}." +
                     "You MUST use JSON for your API response"
         )

        val messageContent = messageHistory.map { message ->
            MessageContent(
                role = if (message.isUser) "user" else "assistant",
                content = message.content
            )
        }

        val combinedMessageData = listOf(systemMessage) + messageContent
        return CompletionRequest(
            messages = combinedMessageData,
        )
    }
}