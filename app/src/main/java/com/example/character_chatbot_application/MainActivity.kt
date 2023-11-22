package com.example.character_chatbot_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.character_chatbot_application.Util.RetrofitClient
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.data.models.Message
import com.example.character_chatbot_application.repositorys.GPTRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        Using this to test the repo
        val gptRepository = GPTRepository(RetrofitClient.gptService)

//        Mock Data
        val char = Character(
            userId = 1,
            name = "Jack Sparrow",
            description = "You are drunk capatin sparrow from the disney films",
            goal = "Your goal is just create a realist conversation with the user",
            backgroundContext = "You are a drunken pirate"
        )

//        Mock Data
        val messages = listOf(Message(
            characterId = 0,
            content = "Hello Jack how are you today",
            timestamp = System.currentTimeMillis(),
            isUser = true
        ),
            Message(
                characterId = 0,
                content = "Arr matey, I be feelin' like a sea turtle on land, wobblin' with the waves. How about ye, matey?",
                timestamp = System.currentTimeMillis(),
                isUser = false
            ),
            Message(
                characterId = 0,
                content = "I am great! Who are you and what do you want?",
                timestamp = System.currentTimeMillis(),
                isUser = true
            )
        )

        Log.i("KEYY", BuildConfig.GPT_KEY)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val res = gptRepository.getCompletion(char, messages)
                Log.i("Test", res.toString())
            } catch (e: Exception) {

            }

        }

    }



}