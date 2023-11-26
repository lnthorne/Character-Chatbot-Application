package com.example.messaging_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ChatViewModel : ViewModel() {
    private val client = OkHttpClient()
    private val _messages = MutableLiveData<MutableList<ChatMessage>>()
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    init {
        _messages.value = mutableListOf()
    }

    fun sendMessage(messageText: String) {
        // Add user message
        addMessageToLiveData(ChatMessage(messageText, MessageType.USER))

        // Get response from OpenAI API
        getResponse(messageText) { response ->
            addMessageToLiveData(ChatMessage(response, MessageType.BOT))
        }
    }

    private fun addMessageToLiveData(message: ChatMessage) {
        val currentMessages = _messages.value ?: mutableListOf()
        currentMessages.add(message)
        _messages.postValue(currentMessages)
    }

    private fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = "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"
        val url = "https://api.openai.com/v1/completions"

        val requestBody = """
            {"model": "text-davinci-003",
            "prompt": "$question",
            "max_tokens": 500,
            "temperature": 0
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .header("Content-Type", "application/json; charset=utf-8")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle API call failure
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        // Handle API error response
                        return
                    }
                    val body = response.body?.string()
                    val jsonObject = JSONObject(body)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                    val textResult = jsonArray.getJSONObject(0).getString("text").trim()
                    callback(textResult)
                }
            }
        })
    }
}
