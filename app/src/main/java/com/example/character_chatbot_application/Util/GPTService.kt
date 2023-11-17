package com.example.character_chatbot_application.Util

import retrofit2.http.Headers
import retrofit2.http.POST

interface GPTService {
    @Headers("Auth:")
    @POST("Endpoint")
    suspend fun callGptApi()
}