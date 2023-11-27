package com.example.character_chatbot_application.Util

import com.example.character_chatbot_application.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
interface GPTService {
    @Headers("Authorization: Bearer ${BuildConfig.GPT_KEY}")
    @POST("v1/chat/completions")
    suspend fun callGptApi(@Body completionRequest: CompletionRequest): Response<CompletionResponse>
}

object RequestConstants{
    const val GPT_MODEL = "gpt-3.5-turbo-1106"
    const val MAX_TOKENS = 150
    const val RESPONSE_FORMAT = "json_object"
}

data class CompletionRequest(
    val messages: List<MessageContent>,
    val model: String? = RequestConstants.GPT_MODEL,
    val max_tokens: Int? = RequestConstants.MAX_TOKENS,
    val response_format: Format? = Format(RequestConstants.RESPONSE_FORMAT),
)

data class CompletionResponse(
    val id: String?,
    val data: String?,
    val created: Int?,
    val model: String?,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val message: MessageContent,
    val index: Int?,
    val finish_reason: String?
)

data class MessageContent(
    val role: String,
    val content: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Format(
    val type: String
)