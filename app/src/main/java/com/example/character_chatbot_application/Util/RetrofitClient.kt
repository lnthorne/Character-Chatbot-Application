package com.example.character_chatbot_application.Util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL = "https://api.openai.com/"

    val gptService: GPTService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GPTService::class.java)
    }
}