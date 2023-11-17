package com.example.character_chatbot_application.repositorys

import com.example.character_chatbot_application.Util.GPTService
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.data.models.Message

class GPTRepository(private val gptService: GPTService) {
     fun constructPrompt(
        character: Character?,
        messageHistory: List<Message>
    ): String {
        val contextBuilder = StringBuilder()
        character?.let {
            contextBuilder.appendLine("Character: ${it.name}")
            contextBuilder.appendLine("Description: ${it.description}")
            contextBuilder.appendLine("Background: ${it.backgroundContext}")
            contextBuilder.appendLine("Goals: ${it.goal}")
            contextBuilder.appendLine("--------------------")
        }

        messageHistory.forEach {
            val speaker = if (it.isUser) "User" else "Character"
            contextBuilder.appendLine("$speaker: ${it.content}")
        }
        return contextBuilder.toString()
    }
}