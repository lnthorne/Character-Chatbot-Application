package com.example.character_chatbot_application.Util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private const val BASE_URL = "https://api.openai.com/"

    // Create a Logger
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Set the desired log level (BODY for full logs)
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Build OkHttpClient and include the logging interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val gptService: GPTService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GPTService::class.java)
    }

}